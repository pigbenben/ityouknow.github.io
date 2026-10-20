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

MATE = 30000
MATE_TH = 20000
INF = 10 ** 9
TIME_BUDGET = 0.85
DRAW_PLIES = 200  # 100 rounds without a capture
CHOU_SETUP = 18  # battery that can uncover a check (抽将)
# Undefended rook, horse, or cannon across the river. Larger than a pawn
# (even with its positional bonus) and smaller than winning a horse.
LOOSE_CAMP = 240

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


_START_PIECES = [
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
]
INITIAL_KEY = pieces_key(_START_PIECES)

# First-move choices. Red and Black each draw one at random so the opening
# is not the same every game. Later moves still come from the search.
_RED_OPENING = (
    (1, 7, 4, 7), (7, 7, 4, 7),  # 当头炮
    (1, 7, 3, 7), (7, 7, 5, 7),  # 仕角炮
    (2, 6, 2, 5), (6, 6, 6, 5),  # 仙人指路
    (4, 6, 4, 5),                # 中兵
    (1, 9, 2, 7),                # 马八进七
    (2, 9, 4, 7), (6, 9, 4, 7),  # 飞相
)
_BLACK_OPENING = (
    (1, 2, 4, 2), (7, 2, 4, 2),  # 当头炮
    (1, 2, 3, 2), (7, 2, 5, 2),  # 仕角炮
    (2, 3, 2, 4), (6, 3, 6, 4),  # 进卒
    (4, 3, 4, 4),                # 中卒
    (1, 0, 2, 2), (7, 0, 6, 2),  # 屏风马
    (2, 0, 4, 2), (6, 0, 4, 2),  # 飞象
)
_OPENING_BOOK = None
_opening_rng = random.SystemRandom()


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


def _step(a, b):
    """Step from a toward b, or 0 when the two squares are not on a line."""
    if a % 9 == b % 9:
        return 9 if b > a else -9
    if a // 9 == b // 9:
        return 1 if b > a else -1
    return 0


def _screen_count(a, b):
    step = _step(a, b)
    if not step:
        return -1
    n = 0
    board = BOARD
    s = a + step
    while s != b:
        if board[s]:
            n += 1
        s += step
    return n


def _piece_attacks(fr, to, piece):
    """Does `piece` on fr attack to? Palace and blocking rules included."""
    if fr == to or not piece:
        return False
    t = piece & 7
    board = BOARD
    if t == R:
        return _screen_count(fr, to) == 0
    if t == C:
        return _screen_count(fr, to) == 1
    if t == H:
        for tsq, leg in HORSE_TO[fr]:
            if tsq == to:
                return board[leg] == 0
        return False
    fx = fr % 9
    fy = fr // 9
    tx = to % 9
    ty = to // 9
    if t == P:
        if piece & 8:
            if tx == fx and ty == fy + 1:
                return True
            return fy >= 5 and ty == fy and abs(tx - fx) == 1
        if tx == fx and ty == fy - 1:
            return True
        return fy <= 4 and ty == fy and abs(tx - fx) == 1
    if t == K:
        if abs(fx - tx) + abs(fy - ty) != 1 or not (3 <= tx <= 5):
            return False
        return ty <= 2 if piece & 8 else ty >= 7
    if t == A:
        if abs(fx - tx) != 1 or abs(fy - ty) != 1 or not (3 <= tx <= 5):
            return False
        return ty <= 2 if piece & 8 else ty >= 7
    if t == E:
        if abs(fx - tx) != 2 or abs(fy - ty) != 2:
            return False
        if piece & 8:
            if fy > 4 or ty > 4:
                return False
        elif fy < 5 or ty < 5:
            return False
        return board[((fy + ty) // 2) * 9 + (fx + tx) // 2] == 0
    return False


def _least_attacker(sq, side):
    """Value of the cheapest piece of `side` that attacks sq. King counts as 1."""
    best = None
    board = BOARD
    for fr in PL[side >> 3]:
        p = board[fr]
        if not p or (p & 8) != side:
            continue
        if _piece_attacks(fr, sq, p):
            v = VAL[p & 7] or 1
            if best is None or v < best:
                best = v
    return best


def see_gain(mv):
    """Material left after the capture and one recapture.

    Positive means the capturer is not losing the exchange. A negative value
    means the piece that just captured is eaten back for a loss.
    """
    cap = mv >> 14
    if not cap:
        return 0
    if (cap & 7) == K:
        return MATE
    fr = mv & 127
    to = (mv >> 7) & 127
    board = BOARD
    moving = board[fr]
    if not moving:
        return 0
    side = moving & 8
    victim = VAL[cap & 7]
    attacker = VAL[moving & 7] or 1
    board[fr] = 0
    board[to] = moving
    back = _least_attacker(to, side ^ 8)
    if back is None:
        board[fr] = moving
        board[to] = cap
        return victim
    defend = _least_attacker(to, side)
    board[fr] = moving
    board[to] = cap
    if defend is None:
        return victim - attacker
    return victim - attacker + back


def _best_capture_gain(side):
    """Best material `side` can win at once. A king capture is mate."""
    ksq = KR[side >> 3]
    if ksq < 0:
        return 0
    checked = in_check(side, ksq)
    best = 0
    for mv in gen_pseudo(side):
        cap = mv >> 14
        if not cap or not is_legal(mv, side, checked, ksq):
            continue
        if (cap & 7) == K:
            return MATE
        gain = see_gain(mv)
        if gain > best:
            best = gain
    return best


def _horses_home(side):
    """Both horses still on their original squares."""
    board = BOARD
    if side == RED:
        return board[82] == RED + H and board[88] == RED + H
    return board[1] == BLACK + H and board[7] == BLACK + H


def _loose_camp_penalty(side):
    """Penalty for an undefended rook, horse, or cannon across the river.

    A major that has crossed and is not attacked by one of our own pieces can
    be surrounded before the rest of the army moves. The charge is applied to
    whoever owns the piece, so capturing it still gains the material.
    """
    board = BOARD
    pen = 0
    red = side == RED
    for sq in PL[side >> 3]:
        p = board[sq]
        if not p or (p & 8) != side:
            continue
        t = p & 7
        if t != H and t != R and t != C:
            continue
        y = SQY[sq]
        if red:
            if y > 4:
                continue
        elif y < 5:
            continue
        if _least_attacker(sq, side) is None:
            pen += LOOSE_CAMP
    return pen


def _delivers_mate(mv, side):
    """True when this move checks and the opponent has no legal reply."""
    info = make(mv)
    opp = side ^ 8
    ksq = KR[opp >> 3]
    mate = ksq >= 0 and in_check(opp, ksq) and not gen_legal(opp)
    unmake(*info)
    return mate


def _move_drops_piece(mv, side):
    """True when this move hands the opponent a piece we do not win back.

    A capture that is recaptured for a loss counts. So does any move that
    leaves the opponent a profitable capture of a non-pawn, a cannon trip
    while both horses are still home, or parking a major across the river
    with nothing protecting it just to take a pawn. Mate is kept.
    """
    cap = mv >> 14
    if cap and (cap & 7) == K:
        return False
    gained = see_gain(mv) if cap else 0
    if cap and gained < 0:
        return not _delivers_mate(mv, side)
    moving = BOARD[mv & 127]
    cannon_early = (
        _horses_home(side)
        and moving
        and (moving & 7) == C
        and not (cap and VAL[cap & 7] >= 140 and gained >= 0)
    )
    info = make(mv)
    their = _best_capture_gain(side ^ 8)
    loose = _loose_camp_penalty(side)
    unmake(*info)
    # Advisor, elephant, horse, cannon, rook. Not a pawn.
    bad = their >= 140 and their > gained + 30
    # A pawn is not worth an undefended major in the enemy camp.
    if loose and gained < 140:
        bad = True
    if cannon_early:
        bad = True
    if bad and _delivers_mate(mv, side):
        return False
    return bad


def _discover_squares(side):
    """Squares our pieces stand on where leaving reveals a check."""
    ksq = KR[(side ^ 8) >> 3]
    if ksq < 0:
        return ()
    board = BOARD
    found = []
    seen = set()
    for sq in PL[side >> 3]:
        p = board[sq]
        if not p or (p & 8) != side:
            continue
        t = p & 7
        if t != R and t != C:
            continue
        step = _step(sq, ksq)
        if not step:
            continue
        occ = 0
        first = second = -1
        s = sq + step
        while s != ksq:
            if board[s]:
                occ += 1
                if occ == 1:
                    first = s
                elif occ == 2:
                    second = s
                else:
                    break
            s += step
        if t == R and occ == 1 and (board[first] & 8) == side and first not in seen:
            seen.add(first)
            found.append(first)
        elif t == C and occ == 2:
            for s in (first, second):
                if board[s] and (board[s] & 8) == side and s not in seen:
                    seen.add(s)
                    found.append(s)
    ht = side + H
    for hsq, leg in HORSE_FROM[ksq]:
        if board[hsq] == ht and board[leg] and (board[leg] & 8) == side and leg not in seen:
            seen.add(leg)
            found.append(leg)
    return found


def _opens_check(fr, to, side, ksq):
    """True when moving fr -> to uncovers a rook, cannon, or horse check."""
    board = BOARD
    for sq in PL[side >> 3]:
        if sq == fr:
            continue
        p = board[sq]
        if not p or (p & 8) != side:
            continue
        t = p & 7
        if t == R or t == C:
            step = _step(sq, ksq)
            if not step:
                continue
            occ = 0
            on_line = False
            landed = False
            s = sq + step
            while s != ksq:
                if s == fr:
                    on_line = True
                if s == to:
                    landed = True
                if board[s]:
                    occ += 1
                    if occ > 2:
                        break
                s += step
            if not on_line or landed:
                continue
            if t == R and occ == 1:
                return True
            if t == C and occ == 2:
                return True
        elif t == H:
            for hsq, leg in HORSE_FROM[ksq]:
                if hsq == sq and leg == fr and to != fr:
                    return True
    return False


def _chou_target(mv, side, blockers):
    """Value of the enemy piece won by a 抽将, or 0.

    抽将: one piece steps off a line and uncovers check, while that same piece
    captures or attacks a different enemy piece. The opponent has to answer
    the check, so the attacked piece is the one we are trying to win.
    """
    fr = mv & 127
    if fr not in blockers:
        return 0
    ksq = KR[(side ^ 8) >> 3]
    if ksq < 0:
        return 0
    to = (mv >> 7) & 127
    if not _opens_check(fr, to, side, ksq):
        return 0
    cap = mv >> 14
    board = BOARD
    moving = board[fr]
    best = 0
    if cap and (cap & 7) != K:
        best = VAL[cap & 7]
    board[fr] = 0
    board[to] = moving
    opp = side ^ 8
    for sq in PL[opp >> 3]:
        if sq == to:
            continue
        p = board[sq]
        if not p or (p & 8) != opp or (p & 7) == K:
            continue
        if _piece_attacks(to, sq, moving):
            v = VAL[p & 7]
            if v > best:
                best = v
    board[fr] = moving
    board[to] = cap
    return best


def _chou_setup(ksq, side):
    """Small bonus for a battery that can uncover check on the enemy king."""
    if ksq < 0:
        return 0
    board = BOARD
    bonus = 0
    for sq in PL[side >> 3]:
        p = board[sq]
        if not p or (p & 8) != side:
            continue
        t = p & 7
        if t != R and t != C:
            continue
        step = _step(sq, ksq)
        if not step:
            continue
        occ = 0
        ours = 0
        s = sq + step
        while s != ksq:
            p2 = board[s]
            if p2:
                occ += 1
                if (p2 & 8) == side:
                    ours += 1
                if occ > 2:
                    break
            s += step
        if t == R and occ == 1 and ours == 1:
            bonus += CHOU_SETUP
        elif t == C and occ == 2 and ours:
            bonus += CHOU_SETUP
    ht = side + H
    for hsq, leg in HORSE_FROM[ksq]:
        if board[hsq] == ht and board[leg] and (board[leg] & 8) == side:
            bonus += CHOU_SETUP
    return bonus


def _early_cannon(side):
    """Penalty for moving a cannon before either horse has left home.

    Both cannons start on their own pawn line. Leaving that square while both
    horses are still on the back rank is what lets the opponent take the
    cannons before the rest of the army has moved.
    """
    board = BOARD
    if side == RED:
        if board[82] != RED + H or board[88] != RED + H:
            return 0
        home = (64, 70)  # (1,7), (7,7)
        pl = PL[0]
        cannon = RED + C
    else:
        if board[1] != BLACK + H or board[7] != BLACK + H:
            return 0
        home = (19, 25)  # (1,2), (7,2)
        pl = PL[1]
        cannon = BLACK + C
    out = 0
    for sq in pl:
        if board[sq] == cannon and sq not in home:
            out += 140
    return out


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
    score += _chou_setup(kb, RED)
    score -= _chou_setup(kr, BLACK)
    score -= _early_cannon(RED)
    score += _early_cannon(BLACK)
    score -= _loose_camp_penalty(RED)
    score += _loose_camp_penalty(BLACK)
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
    blockers = set(_discover_squares(side))
    scored = []
    append = scored.append
    for mv in moves:
        cap = mv >> 14
        chou = _chou_target(mv, side, blockers) if blockers else 0
        if cap and (cap & 7) == K:
            s = 5000000
        elif mv == prev_best and prev_best:
            s = 4000000
        elif mv == tt_mv and tt_mv:
            s = 3500000
        elif chou:
            # Uncover check and take (or attack) another piece.
            s = 2800000 + chou
        elif cap:
            gain = see_gain(mv)
            if gain >= 0:
                s = 1200000 + gain
            else:
                # Recapture wins the exchange: try this after real moves.
                s = 250000 + gain
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
        # Captures, plus quiet moves that step off a battery. 抽将 is often
        # quiet on this ply and only takes the piece after the check is answered.
        blocks = set(_discover_squares(side))
        moves = []
        for mv in gen_pseudo(side):
            if (mv >> 14) or (blocks and (mv & 127) in blocks):
                moves.append(mv)
        if not moves:
            return stand
    ordered = order_moves(moves, side, ply, 0)
    opp = side ^ 8
    legal_any = False
    best = -INF if checked else alpha
    for s, mv in ordered:
        cap = mv >> 14
        if not checked and s < 500000:
            break
        if not checked and cap and (cap & 7) != K:
            if stand + VAL[cap & 7] + 200 < alpha:
                continue
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


def _branch_factor(npieces, nlegal, prev_dur, older_dur):
    """Guess how much slower the next ply will be.

    The guess is deliberately under the raw branching of a full board, so
    0.85s finishes depth 4 and still spends the rest of the budget on depth 5.
    Fewer pieces, and a narrower root, grow slower, so the guess drops and
    the same budget is allowed to start (and often finish) extra plies.
    """
    if npieces >= 28:
        # Capture safety and 抽将 make each node heavier, so a full board
        # grows faster than 2x. 1.6 still spends the rest of the budget
        # on the next ply instead of returning early.
        guess = 1.6
    elif npieces >= 20:
        guess = 2.1
    elif npieces >= 14:
        guess = 1.95
    elif npieces >= 8:
        guess = 1.7
    else:
        guess = 1.3
    if nlegal <= 6:
        guess = min(guess, 1.55)
    elif nlegal <= 12:
        guess = min(guess, 1.8)
    if older_dur >= 0.01 and prev_dur > 0:
        observed = prev_dur / older_dur
        if 1.2 <= observed < guess:
            guess = max(1.35, observed * 0.95)
    return guess


def search_root(side, max_depth, time_limit, root_nocap, npieces=32):
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
    # Drop moves that hand the opponent a piece, when any move does not.
    # Search then attacks only among the moves that survive the exchange.
    safe = []
    for mv in legal:
        if not _move_drops_piece(mv, side):
            safe.append(mv)
    if safe:
        legal = safe
    nlegal = len(legal)
    if nlegal == 1 or max_depth == 0:
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
    older_dur = 0.0
    limit = max_depth if max_depth else 32
    d = 1
    while d <= limit:
        if d > 1:
            factor = _branch_factor(npieces, nlegal, prev_dur, older_dur)
            if time.perf_counter() + prev_dur * factor > DEADLINE:
                break
        ABORT = False
        st = time.perf_counter()
        sc, mv, done = _root_depth(side, legal, d, best, root_nocap)
        older_dur = prev_dur
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


def _shifted(pieces, mv):
    fx, fy, tx, ty = mv
    out = []
    for pc in pieces:
        if (pc["x"], pc["y"]) == (tx, ty):
            continue
        if (pc["x"], pc["y"]) == (fx, fy):
            out.append({"x": tx, "y": ty, "c": pc["c"], "t": pc["t"]})
        else:
            out.append(pc)
    return out


def _build_opening_book():
    """Map the start, and every position after one red move, to opening choices."""
    book = {INITIAL_KEY: _RED_OPENING}
    load(_START_PIECES, RED)
    for mv in gen_legal(RED):
        fr = mv & 127
        to = (mv >> 7) & 127
        coord = (SQX[fr], SQY[fr], SQX[to], SQY[to])
        book[pieces_key(_shifted(_START_PIECES, coord))] = _BLACK_OPENING
    return book


def _opening_move(side):
    """Random legal choice from the opening list, or None to search."""
    global LAST
    options = _OPENING_BOOK.get(board_key())
    if not options:
        return None
    ksq = KR[side >> 3]
    checked = in_check(side, ksq)
    legal = {}
    for mv in gen_pseudo(side):
        if not is_legal(mv, side, checked, ksq):
            continue
        fr = mv & 127
        to = (mv >> 7) & 127
        legal[(SQX[fr], SQY[fr], SQX[to], SQY[to])] = mv
    choices = [legal[c] for c in options if c in legal]
    if not choices:
        return None
    LAST = (0, 0, 0, 0.0)
    return _opening_rng.choice(choices)


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
    mv = _opening_move(side)
    if mv is None:
        mv = search_root(side, depth, limit, root_nocap, n)
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


_OPENING_BOOK = _build_opening_book()
