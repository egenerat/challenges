import re

import sys

TAB = '    '


def are_parentheses_balanced(text):
    sub_blocks = text.split(";")
    opening_parentheses = sub_blocks[0].count("(")
    closing_parentheses = sub_blocks[0].count(")")
    if not opening_parentheses:
        return False
    for i in sub_blocks[1:]:
        if i.count("(") != opening_parentheses or i.count(")") != closing_parentheses:
            return False
    return True


def shift_text_to_right(lines):
    return [TAB+line for line in lines]


def clean_input(data):
    return "".join([i.strip("\t ") for i in data if i.strip("\t ")])


def extract_blocks_content(text):
    return re.findall("\(([.\S\s]*)\)", text)


def _format_block(text):
    if "(" not in text:
        lines = text.replace(";", "|;")
        return [line.replace("|", ";") for line in lines.split(";") if lines]
    else:
        if ";" in text and are_parentheses_balanced(text):
            blocks_content = text.split(";")
            is_extracted = False
        else:
            blocks_content = extract_blocks_content(text)
            is_extracted = True
        result = []
        if is_extracted:
            result.append("(")
        is_first_block = True
        for block in blocks_content:
            if is_first_block:
                is_first_block = False
            else:
                result[-1] += ";"
            lines = _format_block(block)
            if is_extracted:
                lines = shift_text_to_right(lines)
            for line in lines:
                result.append(line)
        if is_extracted:
            result.append(")")
        return result


def cgxformat(lines):
    inp = clean_input(lines)
    return "\n".join(_format_block(inp))


def initialize_game_input():
    n = int(input())
    lines = []
    for i in range(n):
        cgxline = input()
        lines.append(cgxline)
    return lines


def __debug_input(lines):
    print("="*10, file=sys.stderr)
    for i in lines:
        print(i, file=sys.stderr)
    print("="*10, file=sys.stderr)


if __name__ == '__main__':
    game_input = initialize_game_input()
    __debug_input(game_input)
    result = cgxformat(game_input)
    print(result)

