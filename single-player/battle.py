import sys

CARDS = ['2', '3', '4', '5', '6', '7', '8', '9', '10', 'J', 'Q', 'K', 'A']

deck1 = []
deck2 = []

battledeck1 = []
battledeck2 = []

n = int(input())
for i in range(n):
    cardp_1 = input()
    deck1.append(cardp_1[0])
m = int(input())
for i in range(m):
    cardp_2 = input()
    deck2.append(cardp_2[0])

pat = False

i = 0
while len(deck1) and len(deck2):
    battledeck1.append(deck1.pop(0))
    battledeck2.append(deck2.pop(0))

    card1 = CARDS.index(battledeck1[0])
    card2 = CARDS.index(battledeck2[0])

    battle_winner = None

    if card1 != card2:
        if card1 > card2:
            battle_winner = deck1
        else:
            battle_winner = deck2
    else:
        print("BATTLE", file=sys.stderr)
    battle_winner += battledeck1 + battledeck2
    i+=1

if pat or not (len(deck1) or len(deck2)):
    print("PAT")
else:
    winner = 1 if len(deck1) else 2
    print(f"{winner} {i}")
