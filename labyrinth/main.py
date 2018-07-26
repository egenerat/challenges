import sys


class Player:
    def __init__(self, r, c):
        self.visited = [[False for i in range(c)] for j in range(r)]
        self.control_room = None
        self.reached_control_room = False
        self.path = []

    def set_round_data(self, maze, kr, kc):
        self.maze = maze
        self.kr = kr
        self.kc = kc
        self.visited[kr][kc] = True

        # Check if the control_room is now visible
        if not self.control_room:
            self.control_room = p.get_control_room()
        if (self.kr, self.kc) == self.control_room:
            self.reached_control_room = True
        if not self.reached_control_room:
            self.path.append((kr, kc))
            print(self.path, file=sys.stderr)

    def print_maze(self):
        for i in self.maze:
            print('{}'.format(i), file=sys.stderr)

    def get_control_room(self):
        for idx, row in enumerate(self.maze):
            if 'C' in row:
                return idx, row.index('C')

    def get_available_moves(self):
        # result = []
        if self.kr > 0:
            neighbour = self.maze[self.kr - 1][self.kc]
            if neighbour != '#' and not self.visited[self.kr - 1][self.kc]:
                # result.append((kr - 1, kc), neighbour)
                return "UP"
        if kc < c - 1:
            neighbour = maze[kr][kc + 1]
            if neighbour != '#' and not self.visited[self.kr][self.kc + 1]:
                # result.append((kr, kc + 1), neighbour)
                return "RIGHT"
        if kr < r - 1:
            neighbour = maze[self.kr + 1][self.kc]
            if neighbour != '#' and not self.visited[self.kr + 1][self.kc]:
                # result.append((kr + 1, kc), neighbour)
                return "DOWN"
        if kc > 0:
            neighbour = maze[kr][kc - 1]
            if neighbour != '#' and not self.visited[self.kr][self.kc - 1]:
                # result.append((kr, kc - 1), neighbour)
                return "LEFT"

    def get_next_move(self):
        if not self.reached_control_room:
            next_move = p.get_available_moves()
        else:
            next_r, next_c = self.path.pop()
            next_move = get_move_return(self.kr, self.kc, next_r, next_c)
            print("{}".format(next_move), file=sys.stderr)
        return next_move


def get_move_return(kr, kc, next_r, next_c):
    print("{} {} {} {}".format(kr, kc, next_r, next_c), file=sys.stderr)
    if kr < next_r:
        return "DOWN"
    if kr > next_r:
        return "UP"
    if kc < next_c:
        return "RIGHT"
    if kc > next_c:
        return "LEFT"


if __name__ == '__main__':
    # r: number of rows.
    # c: number of columns.
    # a: number of rounds between the time the alarm countdown is activated and the time the alarm goes off.
    r, c, a = [int(i) for i in input().split()]
    p = Player(r, c)

    while True:
        maze = []
        # kr: row where Kirk is located.
        # kc: column where Kirk is located.
        kr, kc = [int(i) for i in input().split()]
        for i in range(r):
            row = input()
            maze.append(row)
        p.set_round_data(maze, kr, kc)
        p.print_maze()
        next_move = p.get_next_move()
        print(next_move)
