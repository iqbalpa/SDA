import random

N = int(input("Jumlah Pulau: "))
print(N)
for i in range(N):
    angka = random.randint(1, 2)
    if angka == 1:
        print("R", end=" ")
    else:
        print("B", end=" ")