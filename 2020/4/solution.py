import re

with open('input.txt') as f:
    input_ = f.read().strip()

raw_passports = input_.split("\n\n")

def parse_passport(passport):
    key_value_pairs = passport.replace("\n", " ").split(" ")
    return dict([pair.split(":") for pair in key_value_pairs])

passports = list(map(parse_passport, raw_passports))

def has_passport_required_fields(passport):
    required_keys = set(['byr', 'iyr', 'eyr', 'hgt', 'hcl', 'ecl', 'pid'])
    return set(passport.keys()).issuperset(required_keys)

print(len(list(filter(has_passport_required_fields, passports))))

def is_valid_year(value, min, max):
    return bool(re.fullmatch(r"\d{4}", value)) and min <= int(value) <= max

def is_valid_height(value):
    if (m := re.fullmatch(r"(\d+)cm", value)) and 150 <= int(m[1]) <= 193:
        return True
    elif (m := re.fullmatch(r"(\d+)in", value)) and 59 <= int(m[1]) <= 76:
        return True
    else:
        return False

def is_valid_hair_color(value):
    return bool(re.fullmatch(r"#[0-9a-f]{6}", value))

def is_valid_eye_color(value):
    return value in ["amb", "blu", "brn", "gry", "grn", "hzl", "oth"]

def is_valid_passport_id(value):
    return bool(re.fullmatch(r"\d{9}", value))

def is_passport_fully_valid(passport):
    return (
        has_passport_required_fields(passport) and
        is_valid_year(passport["byr"], 1920, 2002) and
        is_valid_year(passport["iyr"], 2010, 2020) and
        is_valid_year(passport["eyr"], 2020, 2030) and
        is_valid_height(passport["hgt"]) and
        is_valid_hair_color(passport["hcl"]) and
        is_valid_eye_color(passport["ecl"]) and
        is_valid_passport_id(passport["pid"])
    )

print(len(list(filter(is_passport_fully_valid, passports))))
