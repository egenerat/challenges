class Graph:
    def __init__(self, oriented):
        self.graph = {}
        self.oriented = oriented
    
    def add_connection(self, begin, end):
        if begin not in self.graph:
            self.graph[begin] = []
        self.graph[begin].append(end)
        if not self.oriented:
            if end not in self.graph:
                self.graph[end] = []
            self.graph[end].append(begin)

if __name__ == '__main__':
	g = Graph(False)
	g.add_connection("a", "b")
	g.add_connection("b", "c")
	print(g.graph)