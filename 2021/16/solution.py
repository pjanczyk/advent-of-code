from __future__ import annotations
from dataclasses import dataclass
from functools import reduce


@dataclass
class Packet:
    version: int


@dataclass
class LiteralPacket(Packet):
    version: int
    value: int


@dataclass
class OperatorPacket(Packet):
    type_id: int
    subpackets: list[Packet]


@dataclass
class Bits:
    bits: str
    pos: int = 0

    @staticmethod
    def fromhex(hex: str) -> Bits:
        integer = int(hex, base=16)
        bits = bin(integer).removeprefix('0b')
        return Bits(bits)

    def read(self, n) -> str:
        bits = self.bits[self.pos:self.pos + n]
        self.pos += n
        return bits


def read_input() -> Bits:
    input_ = open('input.txt').read().strip()
    return Bits.fromhex(input_)


def bits_to_int(bits: str) -> int:
    return int(bits, 2)


def decode_packet(bits: Bits) -> Packet:
    version = bits_to_int(bits.read(3))
    type_id = bits_to_int(bits.read(3))

    if type_id == 4:
        value_bits = ''
        while bits.read(1) == '1':
            value_bits += bits.read(4)
        value_bits += bits.read(4)
        value = bits_to_int(value_bits)
        return LiteralPacket(version, value)
    else:
        length_type_id = bits.read(1)
        if length_type_id == '0':
            subpackets_length = bits_to_int(bits.read(15))
            end_pos = bits.pos + subpackets_length
            subpackets = []
            while bits.pos < end_pos:
                subpackets.append(decode_packet(bits))
            return OperatorPacket(version, type_id, subpackets)
        else:
            subpackets_count = bits_to_int(bits.read(11))
            subpackets = [decode_packet(bits) for _ in range(subpackets_count)]
            return OperatorPacket(version, type_id, subpackets)


def sum_versions(packet: Packet) -> int:
    if isinstance(packet, LiteralPacket):
        return packet.version
    elif isinstance(packet, OperatorPacket):
        return packet.version + sum(sum_versions(p) for p in packet.subpackets)


def evaluate_value(packet: Packet) -> int:
    if isinstance(packet, LiteralPacket):
        return packet.value
    elif isinstance(packet, OperatorPacket):
        operators = {
            0: lambda a, b: a + b,
            1: lambda a, b: a * b,
            2: lambda a, b: min(a, b),
            3: lambda a, b: max(a, b),
            5: lambda a, b: 1 if a > b else 0,
            6: lambda a, b: 1 if a < b else 0,
            7: lambda a, b: 1 if a == b else 0,
        }
        oper = operators[packet.type_id]
        values = [evaluate_value(p) for p in packet.subpackets]
        return reduce(oper, values)


def part1():
    bits = read_input()
    packet = decode_packet(bits)
    return sum_versions(packet)


def part2():
    bits = read_input()
    packet = decode_packet(bits)
    return evaluate_value(packet)


print(part1())
print(part2())
