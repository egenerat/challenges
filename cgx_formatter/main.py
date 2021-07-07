import sys
import math

def clean_line(line):
    return line.lstrip().rstrip()

# def format_mutiple_values(line):


def format_block(block, lshift):
    output = []
    output.append(lshift*4*' ' + "(")
    for l in format_output(block[1:-1]):
        output.append((lshift+1*4)*" " + l)
    output.append(lshift*4*' ' + ")")
    return output

def format_list(block):
    values = block.split(';')
    return [f"{i};" for i in values[:-1]] + [values[-1]]


def format_output(line):
    if line.startswith('('):
        return format_block(line, 0)
    elif line:
        return format_list(line)
    return []


if __name__ == '__main__':    
    received = ""
    n = int(input())
    for i in range(n):
        received += clean_line(input())

    print(received, file=sys.stderr)

    output = format_output(received)

    for i in output:
        print(i)