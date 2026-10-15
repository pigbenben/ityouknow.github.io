# -*- coding: utf-8 -*-
"""Xiangqi (Chinese chess) bot.

Only the Python standard library is used. The sandbox entry point is:

    move(pieces, turn) -> (fx, fy, tx, ty) | None
"""

import random
import time

# Piece types
K, A, E, H, R, C, P = 1, 2, 3, 4, 5, 6, 7
RED, BLACK = 0, 8
TYPE = {"K": K, "A": A, "E": E, "H": H, "R": R, "C": C, "P": P}
INV = {K: "K", A: "A", E: "E", H: "H", R: "R", C: "C", P: "P"}

MATE = 30000
MATE_TH = 20000
INF = 10 ** 9
TIME_BUDGET = 0.85
DRAW_PLIES = 200  # 100 rounds without a capture

SQX = [i % 9 for i in range(90)]
SQY = [i // 9 for i in range(90)]

_rng = random.Random(20261015)
Z = [[_rng.getrandbits(64) for _ in range(90)] for _ in range(16)]
ZSIDE = _rng.getrandbits(64)

# Rays from each square: up, down, left, right (toward the board edge).
RAYS = []
for _sq in range(90):
    _x, _y = _sq % 9, _sq // 9
    _up = [yy * 9 + _x for yy in range(_y - 1, -1, -1)]
    _dn = [yy * 9 + _x for yy in range(_y + 1, 10)]
    _lf = [_y * 9 + xx for xx in range(_x - 1, -1, -1)]
    _rt = [_y * 9 + xx for xx in range(_x + 1, 9)]
    RAYS.append((_up, _dn, _lf, _rt))

HORSE_TO = [[] for _ in range(90)]
HORSE_FROM = [[] for _ in range(90)]
for _y in range(10):
    for _x in range(9):
        _s = _y * 9 + _x
        for _dx, _dy, _lx, _ly in (
            (2, 1, 1, 0), (2, -1, 1, 0), (-2, 1, -1, 0), (-2, -1, -1, 0),
            (1, 2, 0, 1), (-1, 2, 0, 1), (1, -2, 0, -1), (-1, -2, 0, -1),
        ):
            _tx, _ty = _x + _dx, _y + _dy
            if 0 <= _tx < 9 and 0 <= _ty < 10:
                _leg = (_y + _ly) * 9 + (_x + _lx)
                _to = _ty * 9 + _tx
                HORSE_TO[_s].append((_to, _leg))
                HORSE_FROM[_to].append((_s, _leg))

PAWN_DEST = [[], []]
KING_DEST = [[], []]
ADV_DEST = [[], []]
ELE_DEST = [[], []]
for _color in (0, 1):
    _pawn = [[] for _ in range(90)]
    _king = [[] for _ in range(90)]
    _adv = [[] for _ in range(90)]
    _ele = [[] for _ in range(90)]
    _ymin, _ymax = (7, 9) if _color == 0 else (0, 2)
    for _y in range(10):
        for _x in range(9):
            _s = _y * 9 + _x
            if _color == 0:
                if _y > 0:
                    _pawn[_s].append((_y - 1) * 9 + _x)
                if _y <= 4:
                    if _x > 0:
                        _pawn[_s].append(_y * 9 + _x - 1)
                    if _x < 8:
                        _pawn[_s].append(_y * 9 + _x + 1)
            else:
                if _y < 9:
                    _pawn[_s].append((_y + 1) * 9 + _x)
                if _y >= 5:
                    if _x > 0:
                        _pawn[_s].append(_y * 9 + _x - 1)
                    if _x < 8:
                        _pawn[_s].append(_y * 9 + _x + 1)
            if 3 <= _x <= 5 and _ymin <= _y <= _ymax:
                for _dx, _dy in ((1, 0), (-1, 0), (0, 1), (0, -1)):
                    _tx, _ty = _x + _dx, _y + _dy
                    if 3 <= _tx <= 5 and _ymin <= _ty <= _ymax:
                        _king[_s].append(_ty * 9 + _tx)
                for _dx, _dy in ((1, 1), (1, -1), (-1, 1), (-1, -1)):
                    _tx, _ty = _x + _dx, _y + _dy
                    if 3 <= _tx <= 5 and _ymin <= _ty <= _ymax:
                        _adv[_s].append(_ty * 9 + _tx)
            for _dx, _dy in ((2, 2), (2, -2), (-2, 2), (-2, -2)):
                _tx, _ty = _x + _dx, _y + _dy
                if not (0 <= _tx < 9 and 0 <= _ty < 10):
                    continue
                if _color == 0 and (_y < 5 or _ty < 5):
                    continue
                if _color == 1 and (_y > 4 or _ty > 4):
                    continue
                _eye = (_y + _dy // 2) * 9 + (_x + _dx // 2)
                _ele[_s].append((_ty * 9 + _tx, _eye))
    PAWN_DEST[_color] = _pawn
    KING_DEST[_color] = _king
    ADV_DEST[_color] = _adv
    ELE_DEST[_color] = _ele

# Positional tables from Red's point of view (y=0 is the top of the board).
PAWN_ROWS = (
    (55, 70, 85, 110, 130, 110, 85, 70, 55),
    (50, 65, 90, 120, 145, 120, 90, 65, 50),
    (40, 55, 75, 105, 125, 105, 75, 55, 40),
    (28, 40, 55, 80, 95, 80, 55, 40, 28),
    (16, 24, 36, 52, 64, 52, 36, 24, 16),
    (2, 4, 10, 18, 22, 18, 10, 4, 2),
    (0, 0, 2, 8, 10, 8, 2, 0, 0),
    (0, 0, 0, 0, 0, 0, 0, 0, 0),
    (0, 0, 0, 0, 0, 0, 0, 0, 0),
    (0, 0, 0, 0, 0, 0, 0, 0, 0),
)
HORSE_ROWS = (
    (2, 6, 12, 8, 2, 8, 12, 6, 2),
    (4, 12, 22, 16, 8, 16, 22, 12, 4),
    (8, 18, 24, 26, 18, 26, 24, 18, 8),
    (6, 20, 22, 28, 22, 28, 22, 20, 6),
    (4, 14, 18, 22, 18, 22, 18, 14, 4),
    (2, 10, 16, 18, 16, 18, 16, 10, 2),
    (0, 6, 10, 12, 12, 12, 10, 6, 0),
    (0, 4, 8, 8, 6, 8, 8, 4, 0),
    (-2, 2, 4, 4, 2, 4, 4, 2, -2),
    (-6, -8, 0, 0, 0, 0, 0, -8, -6),
)
CANNON_ROWS = (
    (4, 4, 2, -4, -8, -4, 2, 4, 4),
    (2, 2, 2, -2, -10, -2, 2, 2, 2),
    (2, 4, 4, 0, -4, 0, 4, 4, 2),
    (0, 2, 4, 6, 8, 6, 4, 2, 0),
    (0, 2, 4, 6, 10, 6, 4, 2, 0),
    (0, 0, 2, 4, 8, 4, 2, 0, 0),
    (2, 2, 4, 6, 8, 6, 4, 2, 2),
    (4, 2, 8, 8, 16, 8, 8, 2, 4),
    (2, 2, 4, 6, 8, 6, 4, 2, 2),
    (0, 0, 2, 4, 6, 4, 2, 0, 0),
)
ROOK_ROWS = (
    (14, 16, 16, 20, 22, 20, 16, 16, 14),
    (18, 22, 20, 26, 28, 26, 20, 22, 18),
    (14, 16, 16, 22, 24, 22, 16, 16, 14),
    (12, 16, 16, 20, 22, 20, 16, 16, 12),
    (10, 14, 12, 18, 20, 18, 12, 14, 10),
    (8, 12, 12, 16, 18, 16, 12, 12, 8),
    (6, 8, 8, 12, 14, 12, 8, 8, 6),
    (4, 6, 6, 8, 10, 8, 6, 6, 4),
    (2, 4, 4, 6, 8, 6, 4, 4, 2),
    (0, 2, 2, 2, 4, 2, 2, 2, 0),
)
KING_ROWS = (
    (0, 0, 0, 0, 0, 0, 0, 0, 0),
    (0, 0, 0, 0, 0, 0, 0, 0, 0),
    (0, 0, 0, 0, 0, 0, 0, 0, 0),
    (0, 0, 0, 0, 0, 0, 0, 0, 0),
    (0, 0, 0, 0, 0, 0, 0, 0, 0),
    (0, 0, 0, 0, 0, 0, 0, 0, 0),
    (0, 0, 0, 0, 0, 0, 0, 0, 0),
    (0, 0, 0, 8, 12, 8, 0, 0, 0),
    (0, 0, 0, 10, 16, 10, 0, 0, 0),
    (0, 0, 0, 12, 20, 12, 0, 0, 0),
)
ADV_ROWS = (
    (0, 0, 0, 0, 0, 0, 0, 0, 0),
    (0, 0, 0, 0, 0, 0, 0, 0, 0),
    (0, 0, 0, 0, 0, 0, 0, 0, 0),
    (0, 0, 0, 0, 0, 0, 0, 0, 0),
    (0, 0, 0, 0, 0, 0, 0, 0, 0),
    (0, 0, 0, 0, 0, 0, 0, 0, 0),
    (0, 0, 0, 0, 0, 0, 0, 0, 0),
    (0, 0, 0, 16, 0, 16, 0, 0, 0),
    (0, 0, 0, 0, 20, 0, 0, 0, 0),
    (0, 0, 0, 18, 0, 18, 0, 0, 0),
)
ELE_ROWS = (
    (0, 0, 0, 0, 0, 0, 0, 0, 0),
    (0, 0, 0, 0, 0, 0, 0, 0, 0),
    (0, 0, 0, 0, 0, 0, 0, 0, 0),
    (0, 0, 0, 0, 0, 0, 0, 0, 0),
    (0, 0, 0, 0, 0, 0, 0, 0, 0),
    (0, 0, 10, 0, 0, 0, 10, 0, 0),
    (0, 0, 0, 0, 0, 0, 0, 0, 0),
    (8, 0, 0, 0, 12, 0, 0, 0, 8),
    (0, 0, 0, 0, 0, 0, 0, 0, 0),
    (0, 0, 18, 0, 0, 0, 18, 0, 0),
)
_BASE_ROWS = (None, KING_ROWS, ADV_ROWS, ELE_ROWS, HORSE_ROWS, ROOK_ROWS, CANNON_ROWS, PAWN_ROWS)

VAL = [0] * 8
PST_R = [[0] * 90 for _ in range(8)]
PST_B = [[0] * 90 for _ in range(8)]
OPEN_B = 10
EYE_B = 16
BARE_B = 36

BOARD = [0] * 90
KR = [-1, -1]
PI = [-1] * 90
PL = [[], []]
HASH = 0
TT = {}
HIST = [0] * (2 * 90 * 90)
KILL = [[0, 0] for _ in range(96)]
GH = {}
PATH = {}
ABORT = False
DEADLINE = 0.0
NODES = 0
LAST = (0, 0, 0, 0.0)
TT_AGE = 0


def configure(w):
    """Install material values and piece-square tables."""
    global OPEN_B, EYE_B, BARE_B
    VAL[K] = 0
    VAL[A] = w["A"]
    VAL[E] = w["E"]
    VAL[H] = w["H"]
    VAL[R] = w["R"]
    VAL[C] = w["C"]
    VAL[P] = w["P"]
    scale = w.get("pawn_scale", 100) / 100.0
    pst_scale = w.get("pst_scale", 100) / 100.0
    dev = w.get("dev", 0)
    for t in range(1, 8):
        rows = _BASE_ROWS[t]
        for y in range(10):
            for x in range(9):
                v = rows[y][x] * pst_scale
                if t == P:
                    v *= scale
                v = int(v)
                if dev and t in (H, R, C) and y < 9:
                    v += dev
                PST_R[t][y * 9 + x] = v
                PST_B[t][(9 - y) * 9 + x] = v
    OPEN_B = w.get("open", 10)
    EYE_B = w.get("eye", 16)
    BARE_B = w.get("bare", 36)


# Weights chosen by self-play. An aggressive pawn scale with a heavy rook
# beat the balanced setting (2-0 at depth 4, both colors) and then beat
# the nearby pawn/rook/cannon mutations.
DEFAULT = {
    "name": "tuned",
    "R": 1050, "H": 430, "C": 460, "E": 150, "A": 170, "P": 90,
    "pawn_scale": 210, "open": 20, "eye": 24, "bare": 16, "dev": 4,
}


class Mem(object):
    __slots__ = ("tt", "hist", "kill", "gh", "snapshot", "nocap_after", "last_n")

    def __init__(self):
        self.tt = {}
        self.hist = [0] * (2 * 90 * 90)
        self.kill = [[0, 0] for _ in range(96)]
        self.gh = {}
        self.snapshot = None
        self.nocap_after = 0
        self.last_n = None


def reset(mem):
    mem.tt.clear()
    for i in range(len(mem.hist)):
        mem.hist[i] = 0
    mem.kill = [[0, 0] for _ in range(96)]
    mem.gh.clear()
    mem.snapshot = None
    mem.nocap_after = 0
    mem.last_n = None


MEM = Mem()
configure(DEFAULT)


def _bind(mem):
    global TT, HIST, KILL, GH
    TT = mem.tt
    HIST = mem.hist
    KILL = mem.kill
    GH = mem.gh


def load(pieces, side):
    global HASH
    PL[0].clear()
    PL[1].clear()
    kr0 = kr1 = -1
    board = BOARD
    for i in range(90):
        board[i] = 0
        PI[i] = -1
    for pc in pieces:
        x = pc["x"]
        y = pc["y"]
        if not (0 <= x < 9 and 0 <= y < 10):
            continue
        code = TYPE[pc["t"]] + (0 if pc["c"] == "r" else 8)
        sq = y * 9 + x
        board[sq] = code
        si = code >> 3
        PI[sq] = len(PL[si])
        PL[si].append(sq)
        if (code & 7) == K:
            if si == 0:
                kr0 = sq
            else:
                kr1 = sq
    KR[0] = kr0
    KR[1] = kr1
    h = 0
    z = Z
    for sq in range(90):
        p = board[sq]
        if p:
            h ^= z[p][sq]
    if side:
        h ^= ZSIDE
    HASH = h


def board_key():
    return tuple(BOARD)


def pieces_key(pieces):
    b = [0] * 90
    for pc in pieces:
        x = pc["x"]
        y = pc["y"]
        if 0 <= x < 9 and 0 <= y < 10:
            b[y * 9 + x] = TYPE[pc["t"]] + (0 if pc["c"] == "r" else 8)
    return tuple(b)


INITIAL_KEY = pieces_key([
    {"x": 0, "y": 0, "c": "b", "t": "R"}, {"x": 1, "y": 0, "c": "b", "t": "H"},
    {"x": 2, "y": 0, "c": "b", "t": "E"}, {"x": 3, "y": 0, "c": "b", "t": "A"},
    {"x": 4, "y": 0, "c": "b", "t": "K"}, {"x": 5, "y": 0, "c": "b", "t": "A"},
    {"x": 6, "y": 0, "c": "b", "t": "E"}, {"x": 7, "y": 0, "c": "b", "t": "H"},
    {"x": 8, "y": 0, "c": "b", "t": "R"}, {"x": 1, "y": 2, "c": "b", "t": "C"},
    {"x": 7, "y": 2, "c": "b", "t": "C"}, {"x": 0, "y": 3, "c": "b", "t": "P"},
    {"x": 2, "y": 3, "c": "b", "t": "P"}, {"x": 4, "y": 3, "c": "b", "t": "P"},
    {"x": 6, "y": 3, "c": "b", "t": "P"}, {"x": 8, "y": 3, "c": "b", "t": "P"},
    {"x": 0, "y": 6, "c": "r", "t": "P"}, {"x": 2, "y": 6, "c": "r", "t": "P"},
    {"x": 4, "y": 6, "c": "r", "t": "P"}, {"x": 6, "y": 6, "c": "r", "t": "P"},
    {"x": 8, "y": 6, "c": "r", "t": "P"}, {"x": 1, "y": 7, "c": "r", "t": "C"},
    {"x": 7, "y": 7, "c": "r", "t": "C"}, {"x": 0, "y": 9, "c": "r", "t": "R"},
    {"x": 1, "y": 9, "c": "r", "t": "H"}, {"x": 2, "y": 9, "c": "r", "t": "E"},
    {"x": 3, "y": 9, "c": "r", "t": "A"}, {"x": 4, "y": 9, "c": "r", "t": "K"},
    {"x": 5, "y": 9, "c": "r", "t": "A"}, {"x": 6, "y": 9, "c": "r", "t": "E"},
    {"x": 7, "y": 9, "c": "r", "t": "H"}, {"x": 8, "y": 9, "c": "r", "t": "R"},
])


def initial_pieces():
    return [
        {"x": x, "y": y, "c": c, "t": t}
        for (x, y, c, t) in (
            (0, 0, "b", "R"), (1, 0, "b", "H"), (2, 0, "b", "E"), (3, 0, "b", "A"),
            (4, 0, "b", "K"), (5, 0, "b", "A"), (6, 0, "b", "E"), (7, 0, "b", "H"),
            (8, 0, "b", "R"), (1, 2, "b", "C"), (7, 2, "b", "C"),
            (0, 3, "b", "P"), (2, 3, "b", "P"), (4, 3, "b", "P"), (6, 3, "b", "P"),
            (8, 3, "b", "P"),
            (0, 6, "r", "P"), (2, 6, "r", "P"), (4, 6, "r", "P"), (6, 6, "r", "P"),
            (8, 6, "r", "P"), (1, 7, "r", "C"), (7, 7, "r", "C"),
            (0, 9, "r", "R"), (1, 9, "r", "H"), (2, 9, "r", "E"), (3, 9, "r", "A"),
            (4, 9, "r", "K"), (5, 9, "r", "A"), (6, 9, "r", "E"), (7, 9, "r", "H"),
            (8, 9, "r", "R"),
        )
    ]


def export_pieces():
    out = []
    board = BOARD
    for sq in range(90):
        p = board[sq]
        if p:
            out.append({
                "x": SQX[sq], "y": SQY[sq],
                "c": "b" if p & 8 else "r", "t": INV[p & 7],
            })
    return out


def make(mv):
    """Play mv. Piece-list aware. Returns (old_hash, fr, to, cap, piece)."""
    global HASH
    fr = mv & 127
    to = (mv >> 7) & 127
    board = BOARD
    p = board[fr]
    cap = board[to]
    si = p >> 3
    mi = PI[fr]
    PL[si][mi] = to
    PI[fr] = -1
    if cap:
        csi = cap >> 3
        ci = PI[to]
        pl = PL[csi]
        last_i = len(pl) - 1
        if ci != last_i:
            last_sq = pl[last_i]
            pl[ci] = last_sq
            PI[last_sq] = ci
        pl.pop()
    PI[to] = mi
    old = HASH
    h = old ^ Z[p][fr] ^ Z[p][to] ^ ZSIDE
    if cap:
        h ^= Z[cap][to]
    board[fr] = 0
    board[to] = p
    if (p & 7) == K:
        KR[si] = to
    if cap and (cap & 7) == K:
        KR[cap >> 3] = -1
    HASH = h
    return old, fr, to, cap, p


def unmake(old, fr, to, cap, p):
    global HASH
    si = p >> 3
    mi = PI[to]
    PL[si][mi] = fr
    PI[fr] = mi
    PI[to] = -1
    if cap:
        csi = cap >> 3
        PL[csi].append(to)
        PI[to] = len(PL[csi]) - 1
    BOARD[fr] = p
    BOARD[to] = cap
    if (p & 7) == K:
        KR[si] = fr
    if cap and (cap & 7) == K:
        KR[cap >> 3] = to
    HASH = old


def in_check(side, ksq):
    """Enemy attack on our king, including flying generals.

    Advisors and elephants can never reach the enemy palace, so they are skipped.
    """
    if ksq < 0:
        return True
    board = BOARD
    x = SQX[ksq]
    y = SQY[ksq]
    enemy = side ^ 8
    pt = enemy + P
    if side == 0:
        py = y - 1
        if py >= 0 and board[py * 9 + x] == pt:
            return True
        if y >= 5:
            if x > 0 and board[y * 9 + x - 1] == pt:
                return True
            if x < 8 and board[y * 9 + x + 1] == pt:
                return True
    else:
        py = y + 1
        if py <= 9 and board[py * 9 + x] == pt:
            return True
        if y <= 4:
            if x > 0 and board[y * 9 + x - 1] == pt:
                return True
            if x < 8 and board[y * 9 + x + 1] == pt:
                return True
    ht = enemy + H
    for hsq, leg in HORSE_FROM[ksq]:
        if board[hsq] == ht and board[leg] == 0:
            return True
    rt = enemy + R
    ct = enemy + C
    kt = enemy + K
    rays = RAYS[ksq]
    for ri in (0, 1):
        seen = 0
        for to in rays[ri]:
            p = board[to]
            if p:
                if seen == 0:
                    if p == rt or p == kt:
                        return True
                    seen = 1
                else:
                    if p == ct:
                        return True
                    break
    for ri in (2, 3):
        seen = 0
        for to in rays[ri]:
            p = board[to]
            if p:
                if seen == 0:
                    if p == rt:
                        return True
                    seen = 1
                else:
                    if p == ct:
                        return True
                    break
    return False


def gen_pseudo(side):
    board = BOARD
    si = side >> 3
    out = []
    append = out.append
    pawn_d = PAWN_DEST[si]
    king_d = KING_DEST[si]
    adv_d = ADV_DEST[si]
    ele_d = ELE_DEST[si]
    for sq in PL[si]:
        p = board[sq]
        t = p & 7
        if t == P:
            for to in pawn_d[sq]:
                cp = board[to]
                if cp == 0 or (cp & 8) != side:
                    append(sq | (to << 7) | (cp << 14))
        elif t == R:
            for ray in RAYS[sq]:
                for to in ray:
                    cp = board[to]
                    if cp == 0:
                        append(sq | (to << 7))
                    else:
                        if (cp & 8) != side:
                            append(sq | (to << 7) | (cp << 14))
                        break
        elif t == C:
            for ray in RAYS[sq]:
                n = len(ray)
                i = 0
                while i < n:
                    cp = board[ray[i]]
                    if cp:
                        i += 1
                        while i < n:
                            cp2 = board[ray[i]]
                            if cp2:
                                if (cp2 & 8) != side:
                                    to = ray[i]
                                    append(sq | (to << 7) | (cp2 << 14))
                                break
                            i += 1
                        break
                    append(sq | (ray[i] << 7))
                    i += 1
        elif t == H:
            for to, leg in HORSE_TO[sq]:
                if board[leg] == 0:
                    cp = board[to]
                    if cp == 0 or (cp & 8) != side:
                        append(sq | (to << 7) | (cp << 14))
        elif t == E:
            for to, eye in ele_d[sq]:
                if board[eye] == 0:
                    cp = board[to]
                    if cp == 0 or (cp & 8) != side:
                        append(sq | (to << 7) | (cp << 14))
        elif t == A:
            for to in adv_d[sq]:
                cp = board[to]
                if cp == 0 or (cp & 8) != side:
                    append(sq | (to << 7) | (cp << 14))
        else:
            for to in king_d[sq]:
                cp = board[to]
                if cp == 0 or (cp & 8) != side:
                    append(sq | (to << 7) | (cp << 14))
    return out


def is_legal(mv, side, checked, ksq):
    """Geometric moves that do not leave our own king in check."""
    cap = mv >> 14
    if cap and (cap & 7) == K:
        return True
    fr = mv & 127
    to = (mv >> 7) & 127
    board = BOARD
    moving = board[fr]
    if not checked and ksq >= 0 and (moving & 7) != K:
        kx = SQX[ksq]
        ky = SQY[ksq]
        fx = SQX[fr]
        fy = SQY[fr]
        if fx != kx and fy != ky:
            tx = SQX[to]
            ty = SQY[to]
            if tx != kx and ty != ky and (abs(fx - kx) != 1 or abs(fy - ky) != 1):
                return True
    board[fr] = 0
    board[to] = moving
    si = side >> 3
    if (moving & 7) == K:
        KR[si] = to
        nk = to
    else:
        nk = ksq
    bad = in_check(side, nk)
    board[fr] = moving
    board[to] = cap
    if (moving & 7) == K:
        KR[si] = ksq
    return not bad


def gen_legal(side):
    ksq = KR[side >> 3]
    checked = in_check(side, ksq)
    legal = []
    for mv in gen_pseudo(side):
        if is_legal(mv, side, checked, ksq):
            legal.append(mv)
    return legal


def _eye(ksq, cannon):
    if ksq < 0:
        return 0
    board = BOARD
    n = 0
    for ray in RAYS[ksq]:
        for to in ray:
            p = board[to]
            if p:
                if p == cannon:
                    n += 1
                break
    return n


def evaluate(side):
    board = BOARD
    val = VAL
    pr = PST_R
    pb = PST_B
    score = 0
    fc = [0] * 9
    rr = []
    br = []
    ar = aa = er = ea = 0
    kr = kb = -1
    for sq in PL[0]:
        p = board[sq]
        t = p & 7
        score += val[t] + pr[t][sq]
        fc[SQX[sq]] += 1
        if t == R:
            rr.append(sq)
        elif t == A:
            ar += 1
        elif t == E:
            er += 1
        elif t == K:
            kr = sq
    for sq in PL[1]:
        p = board[sq]
        t = p & 7
        score -= val[t] + pb[t][sq]
        fc[SQX[sq]] += 1
        if t == R:
            br.append(sq)
        elif t == A:
            aa += 1
        elif t == E:
            ea += 1
        elif t == K:
            kb = sq
    ob = OPEN_B
    eb = EYE_B
    for sq in rr:
        c = fc[SQX[sq]]
        if c <= 2:
            score += ob << 1
        elif c == 3:
            score += ob
        if kb >= 0 and SQX[sq] == SQX[kb]:
            score += eb
    for sq in br:
        c = fc[SQX[sq]]
        if c <= 2:
            score -= ob << 1
        elif c == 3:
            score -= ob
        if kr >= 0 and SQX[sq] == SQX[kr]:
            score -= eb
    bb = BARE_B
    if ar < 2:
        score -= bb * (2 - ar)
    if aa < 2:
        score += bb * (2 - aa)
    if er == 0:
        score -= bb
    if ea == 0:
        score += bb
    if eb:
        if kb >= 0:
            score += _eye(kb, RED + C) * eb
        if kr >= 0:
            score -= _eye(kr, BLACK + C) * eb
    if side:
        return -score
    return score


def order_moves(moves, side, ply, tt_mv, prev_best=0):
    board = BOARD
    hist = HIST
    si = side >> 3
    k0 = k1 = 0
    if ply < 96:
        k0 = KILL[ply][0]
        k1 = KILL[ply][1]
    pr = PST_R
    pb = PST_B
    scored = []
    append = scored.append
    for mv in moves:
        cap = mv >> 14
        if cap and (cap & 7) == K:
            s = 5000000
        elif mv == prev_best and prev_best:
            s = 4000000
        elif mv == tt_mv and tt_mv:
            s = 3500000
        elif cap:
            s = 1000000 + (VAL[cap & 7] << 4) - VAL[board[mv & 127] & 7]
        elif mv == k0:
            s = 900000
        elif mv == k1:
            s = 800000
        else:
            fr = mv & 127
            to = (mv >> 7) & 127
            moving = board[fr]
            t = moving & 7
            if moving & 8:
                gain = pb[t][to] - pb[t][fr]
            else:
                gain = pr[t][to] - pr[t][fr]
            s = hist[(si * 90 + fr) * 90 + to] + gain
        append((s, mv))
    scored.sort(reverse=True)
    return scored


def _adj_store(score, ply):
    if score > MATE_TH:
        return score + ply
    if score < -MATE_TH:
        return score - ply
    return score


def _adj_load(score, ply):
    if score > MATE_TH:
        return score - ply
    if score < -MATE_TH:
        return score + ply
    return score


def _note_quiet(mv, side, ply, depth):
    if ply < 96:
        slot = KILL[ply]
        if slot[0] != mv:
            slot[1] = slot[0]
            slot[0] = mv
    fr = mv & 127
    to = (mv >> 7) & 127
    idx = ((side >> 3) * 90 + fr) * 90 + to
    v = HIST[idx] + depth * depth
    if v > 400000:
        v = 400000
    HIST[idx] = v


def qsearch(alpha, beta, side, ply, nocap, qdepth):
    global NODES, ABORT
    NODES += 1
    if ABORT:
        return 0
    if (NODES & 2047) == 0 and time.perf_counter() >= DEADLINE:
        ABORT = True
        return 0
    if nocap >= DRAW_PLIES:
        return 0
    ksq = KR[side >> 3]
    checked = in_check(side, ksq)
    stand = 0
    if checked:
        if qdepth < -4:
            return evaluate(side) - 250
        moves = gen_pseudo(side)
        if not moves:
            return -MATE + ply
    else:
        stand = evaluate(side)
        if stand >= beta:
            return stand
        if stand > alpha:
            alpha = stand
        if qdepth <= 0:
            return stand
        moves = [mv for mv in gen_pseudo(side) if mv >> 14]
        if not moves:
            return stand
    ordered = order_moves(moves, side, ply, 0)
    opp = side ^ 8
    legal_any = False
    best = -INF if checked else alpha
    for _, mv in ordered:
        cap = mv >> 14
        if not checked and cap and (cap & 7) != K:
            if stand + VAL[cap & 7] + 200 < alpha:
                break
        if not is_legal(mv, side, checked, ksq):
            continue
        legal_any = True
        if cap and (cap & 7) == K:
            return MATE - ply - 1
        old, fr, to, cap2, p = make(mv)
        child_nocap = 0 if cap2 else nocap + 1
        h = HASH
        pc = PATH.get(h, 0) + 1
        PATH[h] = pc
        if GH.get(h, 0) + pc >= 3 or child_nocap >= DRAW_PLIES:
            sc = 0
        else:
            sc = -qsearch(-beta, -alpha, opp, ply + 1, child_nocap, qdepth - 1)
        if pc == 1:
            del PATH[h]
        else:
            PATH[h] = pc - 1
        unmake(old, fr, to, cap2, p)
        if ABORT:
            return 0
        if sc > best:
            best = sc
        if sc > alpha:
            alpha = sc
            if alpha >= beta:
                break
    if checked and not legal_any:
        return -MATE + ply
    return best


def negamax(depth, alpha, beta, side, ply, nocap, ext_left):
    global NODES, ABORT
    NODES += 1
    if ABORT:
        return 0
    if (NODES & 2047) == 0 and time.perf_counter() >= DEADLINE:
        ABORT = True
        return 0
    if nocap >= DRAW_PLIES:
        return 0

    alpha_orig = alpha
    ent = TT.get(HASH)
    tt_mv = 0
    if ent is not None:
        ed, es, ef, em, eage = ent
        tt_mv = em
        # Scores from a previous iteration have a different horizon and must
        # not be mixed into this one. The stored move is still a good guess.
        if eage == TT_AGE and ed >= depth:
            es = _adj_load(es, ply)
            if ef == 0 or (ef == 1 and es >= beta) or (ef == 2 and es <= alpha):
                return es

    ksq = KR[side >> 3]
    checked = in_check(side, ksq)
    if checked and ext_left > 0:
        depth += 1
        ext_left -= 1
    if depth <= 0:
        return qsearch(alpha, beta, side, ply, nocap, 6)

    moves = gen_pseudo(side)
    if not moves:
        return -MATE + ply
    ordered = order_moves(moves, side, ply, tt_mv)
    opp = side ^ 8
    best_sc = -INF
    best_mv = 0
    legal_any = False
    idx = 0
    for _, mv in ordered:
        if not is_legal(mv, side, checked, ksq):
            continue
        legal_any = True
        cap = mv >> 14
        if cap and (cap & 7) == K:
            sc = MATE - ply - 1
        else:
            old, fr, to, cap2, p = make(mv)
            child_nocap = 0 if cap2 else nocap + 1
            h = HASH
            pc = PATH.get(h, 0) + 1
            PATH[h] = pc
            if GH.get(h, 0) + pc >= 3 or child_nocap >= DRAW_PLIES:
                sc = 0
            else:
                # Full-depth principal variation search. Reducing late quiet moves
                # made shallow scores depend on move order, which is too noisy here.
                if idx == 0:
                    sc = -negamax(depth - 1, -beta, -alpha, opp, ply + 1, child_nocap, ext_left)
                else:
                    sc = -negamax(depth - 1, -alpha - 1, -alpha, opp, ply + 1, child_nocap, ext_left)
                    if not ABORT and alpha < sc < beta:
                        sc = -negamax(depth - 1, -beta, -alpha, opp, ply + 1, child_nocap, ext_left)
            if pc == 1:
                del PATH[h]
            else:
                PATH[h] = pc - 1
            unmake(old, fr, to, cap2, p)
        if ABORT:
            return 0
        if sc > best_sc:
            best_sc = sc
            best_mv = mv
        if sc > alpha:
            alpha = sc
            if alpha >= beta:
                if not cap:
                    _note_quiet(mv, side, ply, depth)
                break
        idx += 1
    if not legal_any:
        return -MATE + ply
    if best_mv and not ABORT:
        if best_sc <= alpha_orig:
            flag = 2
        elif best_sc >= beta:
            flag = 1
        else:
            flag = 0
        _tt_store(depth, best_sc, flag, best_mv, ply)
    return best_sc


def _tt_store(depth, score, flag, mv, ply):
    old = TT.get(HASH)
    if old is not None and old[4] == TT_AGE and old[0] > depth:
        return
    TT[HASH] = (depth, _adj_store(score, ply), flag, mv, TT_AGE)


def _root_depth(side, legal, depth, prev_best, root_nocap):
    global ABORT, TT_AGE
    TT_AGE += 1
    ent = TT.get(HASH)
    tt_mv = ent[3] if ent is not None else 0
    ordered = order_moves(legal, side, 0, tt_mv, prev_best)
    alpha = -INF
    beta = INF
    best_sc = -INF
    best_mv = ordered[0][1]
    finished_mv = None
    finished_sc = -INF
    opp = side ^ 8
    for i, (_, mv) in enumerate(ordered):
        if i > 0 and time.perf_counter() >= DEADLINE:
            ABORT = True
            break
        cap = mv >> 14
        if cap and (cap & 7) == K:
            sc = MATE - 1
            old = None
        else:
            old, fr, to, cap2, p = make(mv)
            child_nocap = 0 if cap2 else root_nocap + 1
            h = HASH
            pc = PATH.get(h, 0) + 1
            PATH[h] = pc
            if GH.get(h, 0) + pc >= 3 or child_nocap >= DRAW_PLIES:
                sc = 0
            elif i == 0:
                sc = -negamax(depth - 1, -beta, -alpha, opp, 1, child_nocap, 2)
            else:
                sc = -negamax(depth - 1, -alpha - 1, -alpha, opp, 1, child_nocap, 2)
                if not ABORT and alpha < sc < beta:
                    sc = -negamax(depth - 1, -beta, -alpha, opp, 1, child_nocap, 2)
            if pc == 1:
                del PATH[h]
            else:
                PATH[h] = pc - 1
        if old is not None:
            unmake(old, fr, to, cap2, p)
        if ABORT:
            break
        if sc > finished_sc:
            finished_sc = sc
            finished_mv = mv
        if sc > best_sc:
            best_sc = sc
            best_mv = mv
        if sc > alpha:
            alpha = sc
    if not ABORT and best_mv:
        _tt_store(depth, best_sc, 0, best_mv, 0)
        return best_sc, best_mv, True
    if finished_mv is not None:
        return finished_sc, finished_mv, False
    return best_sc, None, False


def search_root(side, max_depth, time_limit, root_nocap):
    global ABORT, DEADLINE, NODES, LAST, PATH
    PATH.clear()
    ksq = KR[side >> 3]
    checked = in_check(side, ksq)
    legal = []
    for mv in gen_pseudo(side):
        if is_legal(mv, side, checked, ksq):
            legal.append(mv)
    if not legal:
        LAST = (0, 0, 0, 0.0)
        return None
    for mv in legal:
        cap = mv >> 14
        if cap and (cap & 7) == K:
            LAST = (0, 1, MATE, 0.0)
            return mv
    if len(legal) == 1 or max_depth == 0:
        LAST = (0, 1, 0, 0.0)
        return legal[0]

    DEADLINE = time.perf_counter() + time_limit
    ABORT = False
    NODES = 0
    best = legal[0]
    best_score = -INF
    depth_done = 0
    t0 = time.perf_counter()
    prev_dur = 0.0
    limit = max_depth if max_depth else 32
    d = 1
    while d <= limit:
        if d > 1 and time.perf_counter() + prev_dur * 2.4 > DEADLINE:
            break
        ABORT = False
        st = time.perf_counter()
        sc, mv, done = _root_depth(side, legal, d, best, root_nocap)
        prev_dur = time.perf_counter() - st
        if mv is not None:
            best = mv
            best_score = sc
        if not done:
            break
        depth_done = d
        if sc > MATE - 800:
            break
        d += 1
    LAST = (depth_done, NODES, best_score, time.perf_counter() - t0)
    return best


def think(pieces, turn, mem, weights, depth=None, time_limit=None):
    """Search and return (fx, fy, tx, ty) or None. Updates mem history."""
    _bind(mem)
    configure(weights)
    side = RED if turn == "r" else BLACK
    n = 0
    for pc in pieces:
        x = pc.get("x", -1)
        y = pc.get("y", -1)
        if 0 <= x < 9 and 0 <= y < 10:
            n += 1
    if mem.last_n is None:
        root_nocap = 0
    elif n < mem.last_n:
        root_nocap = 0
    else:
        root_nocap = mem.nocap_after + 1
    load(pieces, side)
    if len(TT) > 250000:
        TT.clear()
    h = HASH
    GH[h] = GH.get(h, 0) + 1
    limit = TIME_BUDGET if time_limit is None else time_limit
    mv = search_root(side, depth, limit, root_nocap)
    if mv is None:
        mem.snapshot = None
        mem.last_n = n
        mem.nocap_after = root_nocap
        return None
    cap = mv >> 14
    old, fr, to, cap2, p = make(mv)
    GH[HASH] = GH.get(HASH, 0) + 1
    mem.nocap_after = 0 if cap2 else root_nocap + 1
    mem.last_n = n - (1 if cap2 else 0)
    mem.snapshot = (tuple(BOARD), KR[0], KR[1], side ^ 8)
    unmake(old, fr, to, cap2, p)
    return (SQX[fr], SQY[fr], SQX[to], SQY[to])


def _follows(mem, pieces, turn):
    snap = mem.snapshot
    if snap is None:
        return False
    saved, kr0, kr1, opp = snap
    cur = RED if turn == "r" else BLACK
    if opp != (cur ^ 8):
        return False
    target = pieces_key(pieces)
    board = BOARD
    for i in range(90):
        board[i] = saved[i]
    # Rebuild piece lists from the saved board so legality checks are unused;
    # comparison only needs make/unmake, which requires a valid piece list.
    PL[0].clear()
    PL[1].clear()
    for i in range(90):
        PI[i] = -1
    for sq in range(90):
        p = saved[sq]
        if p:
            si = p >> 3
            PI[sq] = len(PL[si])
            PL[si].append(sq)
    KR[0] = kr0
    KR[1] = kr1
    ksq = KR[opp >> 3]
    checked = in_check(opp, ksq)
    for mv in gen_pseudo(opp):
        if not is_legal(mv, opp, checked, ksq):
            continue
        info = make(mv)
        ok = board_key() == target
        unmake(*info)
        if ok:
            return True
    return False


def move(pieces, turn):
    """Return one move (fx, fy, tx, ty), or None when no legal move exists.

    pieces: list of {"x", "y", "c", "t"} for every piece still on the board.
    turn: "r" (Red, moves first, forward is y-1) or "b" (Black, forward is y+1).
    Coordinates: x is the file 0..8 from left to right, y is the rank 0..9 from top to bottom.
    """
    if turn not in ("r", "b") or not pieces:
        return None
    new_game = (
        pieces_key(pieces) == INITIAL_KEY and turn == "r"
    ) or not _follows(MEM, pieces, turn)
    if new_game:
        reset(MEM)
    try:
        return think(pieces, turn, MEM, DEFAULT, depth=None, time_limit=TIME_BUDGET)
    except Exception:
        side = RED if turn == "r" else BLACK
        try:
            load(pieces, side)
            legal = gen_legal(side)
        except Exception:
            return None
        if not legal:
            return None
        mv = legal[0]
        fr = mv & 127
        to = (mv >> 7) & 127
        return (SQX[fr], SQY[fr], SQX[to], SQY[to])


def perft(depth, side):
    if depth == 0:
        return 1
    ksq = KR[side >> 3]
    checked = in_check(side, ksq)
    moves = []
    caps = 0
    for mv in gen_pseudo(side):
        if is_legal(mv, side, checked, ksq):
            moves.append(mv)
            if mv >> 14:
                caps += 1
    if depth == 1:
        return len(moves), caps
    nodes = 0
    captures = 0
    opp = side ^ 8
    for mv in moves:
        info = make(mv)
        n, c = perft(depth - 1, opp)
        nodes += n
        captures += c
        unmake(*info)
    return nodes, captures


def _assert_lists():
    seen = set()
    for si in (0, 1):
        for i, sq in enumerate(PL[si]):
            assert PI[sq] == i, (si, i, sq, PI[sq])
            assert BOARD[sq] and (BOARD[sq] >> 3) == si
            seen.add(sq)
    for sq in range(90):
        if BOARD[sq]:
            assert sq in seen
        else:
            assert PI[sq] == -1


def self_check():
    """Rule checks plus the standard opening perft numbers (44, 1920)."""
    configure(DEFAULT)
    pieces = initial_pieces()
    load(pieces, RED)
    _assert_lists()
    n1, c1 = perft(1, RED)
    assert n1 == 44 and c1 == 2, (n1, c1)
    # The two opening captures are cannon-over-cannon taking the horse.
    legal = gen_legal(RED)
    coords = set()
    for mv in legal:
        fr = mv & 127
        to = (mv >> 7) & 127
        coords.add((SQX[fr], SQY[fr], SQX[to], SQY[to]))
    assert (1, 7, 1, 0) in coords and (7, 7, 7, 0) in coords

    # Horse leg. A pawn on (1, 8) blocks the upward leg; an elephant on (2, 9)
    # blocks the rightward leg. Kings sit on different files.
    load([{"x": 1, "y": 9, "c": "r", "t": "H"},
          {"x": 4, "y": 9, "c": "r", "t": "K"},
          {"x": 3, "y": 0, "c": "b", "t": "K"},
          {"x": 1, "y": 8, "c": "r", "t": "P"},
          {"x": 2, "y": 9, "c": "r", "t": "E"}], RED)
    horse_moves = []
    for mv in gen_legal(RED):
        if (BOARD[mv & 127] & 7) == H:
            to = (mv >> 7) & 127
            horse_moves.append((SQX[to], SQY[to]))
    assert horse_moves == [], horse_moves
    load([{"x": 1, "y": 9, "c": "r", "t": "H"},
          {"x": 4, "y": 9, "c": "r", "t": "K"},
          {"x": 3, "y": 0, "c": "b", "t": "K"}], RED)
    horse_moves = []
    for mv in gen_legal(RED):
        if (BOARD[mv & 127] & 7) == H:
            to = (mv >> 7) & 127
            horse_moves.append((SQX[to], SQY[to]))
    assert sorted(horse_moves) == [(0, 7), (2, 7), (3, 8)], horse_moves

    # Elephant eye and river. The blocked eye removes (4, 7); (0, 7) stays.
    load([{"x": 2, "y": 9, "c": "r", "t": "E"},
          {"x": 4, "y": 9, "c": "r", "t": "K"},
          {"x": 3, "y": 0, "c": "b", "t": "K"},
          {"x": 3, "y": 8, "c": "b", "t": "P"}], RED)
    ele = []
    for mv in gen_legal(RED):
        if (BOARD[mv & 127] & 7) == E:
            to = (mv >> 7) & 127
            ele.append((SQX[to], SQY[to]))
    assert ele == [(0, 7)], ele

    # Red pawn before and after the river.
    load([{"x": 4, "y": 5, "c": "r", "t": "P"},
          {"x": 4, "y": 9, "c": "r", "t": "K"},
          {"x": 3, "y": 0, "c": "b", "t": "K"}], RED)
    pm = []
    for mv in gen_legal(RED):
        if (BOARD[mv & 127] & 7) == P:
            to = (mv >> 7) & 127
            pm.append((SQX[to], SQY[to]))
    assert pm == [(4, 4)], pm
    load([{"x": 4, "y": 4, "c": "r", "t": "P"},
          {"x": 4, "y": 9, "c": "r", "t": "K"},
          {"x": 3, "y": 0, "c": "b", "t": "K"}], RED)
    pm = []
    for mv in gen_legal(RED):
        if (BOARD[mv & 127] & 7) == P:
            to = (mv >> 7) & 127
            pm.append((SQX[to], SQY[to]))
    assert sorted(pm) == [(3, 4), (4, 3), (5, 4)], pm

    # Flying generals: the rook screening the two kings cannot leave the file.
    # The king itself may step aside, which also breaks the face-off.
    load([{"x": 4, "y": 9, "c": "r", "t": "K"},
          {"x": 4, "y": 5, "c": "r", "t": "R"},
          {"x": 4, "y": 0, "c": "b", "t": "K"}], RED)
    rook_off = []
    for mv in gen_legal(RED):
        fr = mv & 127
        to = (mv >> 7) & 127
        if (BOARD[fr] & 7) == R and SQX[to] != 4:
            rook_off.append((SQX[to], SQY[to]))
    assert rook_off == [], rook_off

    # Mate in one. Rook (0,1)->(0,0) checks on the back rank. The horse on
    # (5,3) covers the flight square (4,1). Kings are on different files.
    mate_pos = [
        {"x": 4, "y": 0, "c": "b", "t": "K"},
        {"x": 0, "y": 1, "c": "r", "t": "R"},
        {"x": 5, "y": 3, "c": "r", "t": "H"},
        {"x": 3, "y": 9, "c": "r", "t": "K"},
        {"x": 8, "y": 6, "c": "r", "t": "P"},
    ]
    reset(MEM)
    found = think(mate_pos, "r", MEM, DEFAULT, depth=3, time_limit=5)
    assert found == (0, 1, 0, 0), found
    load(mate_pos, RED)
    info = make((1 * 9 + 0) | (0 << 7))
    assert gen_legal(BLACK) == [], [((mv & 127), (mv >> 7) & 127) for mv in gen_legal(BLACK)]
    unmake(*info)

    # Do not trade a rook for a protected horse.
    trap = [
        {"x": 4, "y": 9, "c": "r", "t": "K"},
        {"x": 4, "y": 0, "c": "b", "t": "K"},
        {"x": 3, "y": 0, "c": "b", "t": "A"},
        {"x": 5, "y": 0, "c": "b", "t": "A"},
        {"x": 7, "y": 7, "c": "r", "t": "R"},
        {"x": 7, "y": 4, "c": "b", "t": "H"},
        {"x": 8, "y": 4, "c": "b", "t": "R"},
        {"x": 0, "y": 9, "c": "r", "t": "P"},
    ]
    reset(MEM)
    choice = think(trap, "r", MEM, DEFAULT, depth=2, time_limit=5)
    assert choice != (7, 7, 7, 4), choice

    # Iterative deepening must agree with a single fixed-depth search.
    # Mixing horizons from earlier iterations used to invent a fake advantage.
    reset(MEM)
    _bind(MEM)
    configure(DEFAULT)
    load(pieces, RED)
    legal = gen_legal(RED)
    DEADLINE_SAVE = time.perf_counter() + 30
    global DEADLINE, ABORT, NODES, PATH, TT_AGE
    DEADLINE = DEADLINE_SAVE
    ABORT = False
    NODES = 0
    PATH.clear()
    TT.clear()
    sc_clean, mv_clean, done_clean = _root_depth(RED, legal, 4, 0, 0)
    assert done_clean
    TT.clear()
    prev = 0
    sc_id = None
    mv_id = None
    for d in range(1, 5):
        ABORT = False
        PATH.clear()
        sc_id, mv_id, done_id = _root_depth(RED, legal, d, prev, 0)
        assert done_id
        prev = mv_id
    # Tied opening moves may differ; the score must not. A horizon bug used to
    # make iterative deepening disagree with a single fixed-depth search.
    assert sc_id == sc_clean, (sc_id, sc_clean)
    TT.clear()
    ABORT = False
    PATH.clear()
    info = make(mv_id)
    lone = -negamax(3, -INF, INF, BLACK, 1, 1, 2)
    unmake(*info)
    assert lone == sc_clean, lone

    # A free cannon on an open file is taken. The pawn on the king file
    # stops the two generals from facing each other.
    free = [
        {"x": 4, "y": 9, "c": "r", "t": "K"},
        {"x": 4, "y": 0, "c": "b", "t": "K"},
        {"x": 4, "y": 5, "c": "r", "t": "P"},
        {"x": 0, "y": 9, "c": "r", "t": "R"},
        {"x": 0, "y": 4, "c": "b", "t": "C"},
        {"x": 3, "y": 0, "c": "b", "t": "A"},
        {"x": 5, "y": 0, "c": "b", "t": "A"},
    ]
    reset(MEM)
    got = think(free, "r", MEM, DEFAULT, depth=2, time_limit=5)
    assert got == (0, 9, 0, 4), got

    load(pieces, RED)
    t0 = time.perf_counter()
    n2, c2 = perft(2, RED)
    dt = time.perf_counter() - t0
    assert (n2, c2) == (1920, 72), (n2, c2, dt)
    _assert_lists()
    return n1, n2, dt


if __name__ == "__main__":
    t0 = time.perf_counter()
    a, b, dt = self_check()
    print("perft1", a, "perft2", b, "perft2_sec", round(dt, 3), "total", round(time.perf_counter() - t0, 3))
