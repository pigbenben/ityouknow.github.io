# -*- coding: utf-8 -*-
"""Self-play between evaluation settings. Best of three, two wins required."""

import random
import time

import xiangqi_bot as xb


def _clone(name, **over):
    w = {
        "name": name,
        "R": 1000, "H": 430, "C": 460, "E": 180, "A": 200, "P": 90,
        "pawn_scale": 100, "open": 10, "eye": 16, "bare": 36, "dev": 4,
        "pst_scale": 100,
    }
    w.update(over)
    return w


VARIANTS = [
    _clone("classic"),
    _clone("cannon", C=540, H=390, R=960, eye=32, dev=2),
    _clone("horse", H=530, C=410, dev=12, eye=8, open=6),
    _clone("attack", pawn_scale=170, R=1100, E=140, A=160, bare=18, open=18, eye=22),
    _clone("solid", E=260, A=260, bare=90, R=900, pawn_scale=70, eye=8, dev=0, open=6),
    _clone("material", pst_scale=0, open=0, eye=0, bare=0, dev=0),
]


def apply_move(pieces, turn, mv):
    side = xb.RED if turn == "r" else xb.BLACK
    xb.load(pieces, side)
    found = None
    for m in xb.gen_legal(side):
        fr = m & 127
        to = (m >> 7) & 127
        if (xb.SQX[fr], xb.SQY[fr], xb.SQX[to], xb.SQY[to]) == mv:
            found = m
            break
    if found is None:
        return None, 0
    cap = found >> 14
    xb.make(found)
    if cap and (cap & 7) == xb.K:
        return xb.export_pieces(), "king"
    return xb.export_pieces(), cap


def material(pieces):
    score = 0
    for pc in pieces:
        v = xb.VAL[xb.TYPE[pc["t"]]]
        score += v if pc["c"] == "r" else -v
    return score


def play_game(w_red, w_black, depth, max_plies=160, seed=0):
    mem_r, mem_b = xb.Mem(), xb.Mem()
    xb.reset(mem_r)
    xb.reset(mem_b)
    pieces = xb.initial_pieces()
    turn = "r"
    seen = {}
    nocap = 0
    for ply in range(max_plies):
        key = (xb.pieces_key(pieces), turn)
        seen[key] = seen.get(key, 0) + 1
        if seen[key] >= 3:
            return "draw", "repeat", ply, material(pieces)
        if nocap >= xb.DRAW_PLIES:
            return "draw", "nocap", ply, material(pieces)
        mem = mem_r if turn == "r" else mem_b
        weights = w_red if turn == "r" else w_black
        mv = xb.think(pieces, turn, mem, weights, depth=depth, time_limit=30)
        if mv is None:
            return ("b" if turn == "r" else "r"), "nomove", ply, material(pieces)
        pieces2, cap = apply_move(pieces, turn, mv)
        if pieces2 is None:
            return ("b" if turn == "r" else "r"), "illegal", ply, material(pieces)
        if cap == "king":
            return turn, "king", ply + 1, material(pieces2)
        if cap:
            nocap = 0
        else:
            nocap += 1
        pieces = pieces2
        turn = "b" if turn == "r" else "r"
    return "draw", "maxply", max_plies, material(pieces)


def match(w1, w2, depth, games=3):
    """First to 2 wins, at most `games` games. Colors alternate."""
    wins = {w1["name"]: 0, w2["name"]: 0}
    details = []
    for i in range(games):
        if i % 2 == 0:
            red, black = w1, w2
        else:
            red, black = w2, w1
        t0 = time.perf_counter()
        result, why, ply, mat = play_game(red, black, depth)
        dt = time.perf_counter() - t0
        if result == "r":
            wins[red["name"]] += 1
            winner = red["name"]
        elif result == "b":
            wins[black["name"]] += 1
            winner = black["name"]
        else:
            winner = "draw"
        details.append((winner, why, ply, mat, round(dt, 1)))
        print(
            f"  g{i+1} red={red['name']:8} black={black['name']:8} "
            f"-> {winner:8} {why:7} ply={ply:<3} mat={mat:<5} {dt:.1f}s",
            flush=True,
        )
        if wins[w1["name"]] >= 2 or wins[w2["name"]] >= 2:
            break
    return wins, details


def play_random(weights, depth, games=2, max_plies=80):
    rng = random.Random(1)
    wins = 0
    for i in range(games):
        mem = xb.Mem()
        xb.reset(mem)
        pieces = xb.initial_pieces()
        engine_red = (i % 2 == 0)
        turn = "r"
        nocap = 0
        seen = {}
        winner = "draw"
        for ply in range(max_plies):
            key = (xb.pieces_key(pieces), turn)
            seen[key] = seen.get(key, 0) + 1
            if seen[key] >= 3 or nocap >= xb.DRAW_PLIES:
                winner = "draw"
                break
            engine_turn = (turn == "r") == engine_red
            if engine_turn:
                mv = xb.think(pieces, turn, mem, weights, depth=depth, time_limit=30)
            else:
                side = xb.RED if turn == "r" else xb.BLACK
                xb.load(pieces, side)
                legal = xb.gen_legal(side)
                if not legal:
                    mv = None
                else:
                    m = legal[rng.randrange(len(legal))]
                    fr, to = m & 127, (m >> 7) & 127
                    mv = (xb.SQX[fr], xb.SQY[fr], xb.SQX[to], xb.SQY[to])
            if mv is None:
                winner = "b" if turn == "r" else "r"
                break
            pieces2, cap = apply_move(pieces, turn, mv)
            if pieces2 is None:
                winner = "b" if turn == "r" else "r"
                break
            if cap == "king":
                winner = turn
                break
            nocap = 0 if cap else nocap + 1
            pieces = pieces2
            turn = "b" if turn == "r" else "r"
        else:
            winner = "draw"
        engine_color = "r" if engine_red else "b"
        if winner == engine_color:
            wins += 1
        print(f"  random g{i+1} engine={engine_color} winner={winner}", flush=True)
    return wins


if __name__ == "__main__":
    depth = 3
    print("vs random depth", depth, flush=True)
    rw = play_random(VARIANTS[0], depth, games=2)
    print("engine wins", rw, flush=True)
    names = {w["name"]: w for w in VARIANTS}
    classic = names["classic"]
    for other in ("material", "cannon", "horse", "attack", "solid"):
        print(f"\n== classic vs {other} depth {depth} ==", flush=True)
        wins, _ = match(classic, names[other], depth)
        print(" ", wins, flush=True)
