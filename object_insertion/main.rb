def get_positions(lines)
    shape = Array.new
    lines.each_with_index{ |line, i| 
        line.chars.each_with_index do |c, j|
            if c == '*'
                shape.append([i, j])
            end
        end
    }
    return shape
end

a, b = gets.split(" ").collect { |x| x.to_i }
lines = []
a.times do
    object_line = gets.chomp
    lines.append(object_line)
end


shape = get_positions(lines)

board = []
c, d = gets.split(" ").collect { |x| x.to_i }
c.times do
    grid_line = gets.chomp
    board << grid_line
end

solutions_count = 0
solution = nil
board.each_with_index { | line, row | 
    line.split('').each_with_index { | cell, column| 
        possible = true
        shape.each { | a, b |
            if !board[row + a] || !board[row + a][column + b] || board[row + a] && board[row + a][column + b] == '#'
                possible = false
                break
            end
        }
        if possible
            solutions_count += 1
            solution = [row, column]
        end
    }
}
puts solutions_count
if solutions_count == 1
    r, c = solution
    board.each_with_index { |line, index|
        shape.each { | a, b |
            if r + a == index
                line[c + b] = '*'
            end
        }
        puts line
    }
end
