import sys


class Player:
    def __init__(self, r, c):
        # To keep a state of the map, persistent over turns
        self.visited = [[False for i in range(c)] for j in range(r)]
        self.control_room = None
        self.reached_control_room = False
        self.path = []
        self.maze = None
        self.kr = None
        self.kc = None

    def set_round_data(self, maze, kr, kc):
        self.maze = maze
        self.kr = kr
        self.kc = kc
        self.visited[kr][kc] = True

        # Check if the control_room is now visible
        if not self.control_room:
            self.control_room = p.get_control_room()
        # Check if we have reached the control room
        if (self.kr, self.kc) == self.control_room:
            self.reached_control_room = True
        # Append current position to path
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
        if self.kr > 0:
            neighbour = self.maze[self.kr - 1][self.kc]
            if neighbour != '#' and not self.visited[self.kr - 1][self.kc]:
                return "UP"
        if kc < c - 1:
            neighbour = self.maze[kr][kc + 1]
            if neighbour != '#' and not self.visited[self.kr][self.kc + 1]:
                return "RIGHT"
        if kr < r - 1:
            neighbour = self.maze[self.kr + 1][self.kc]
            if neighbour != '#' and not self.visited[self.kr + 1][self.kc]:
                return "DOWN"
        if kc > 0:
            neighbour = self.maze[kr][kc - 1]
            if neighbour != '#' and not self.visited[self.kr][self.kc - 1]:
                return "LEFT"

    def get_next_move(self):
        if not self.reached_control_room:
            next_move = p.get_available_moves()
            if not next_move:
                # We remove the current position from the path
                self.path.pop()
                # We come back to the previous position
                next_move = get_move_string(self.kr, self.kc, *self.path.pop())
        else:
            next_r, next_c = self.path.pop()
            next_move = get_move_string(self.kr, self.kc, next_r, next_c)
        return next_move


def get_move_string(kr, kc, next_r, next_c):
    print("Current: {},{} next: {},{}".format(kr, kc, next_r, next_c), file=sys.stderr)
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