import random
num_of_saham = int(input())
for i in range(num_of_saham):
    print(random.randint(1, 200_000), end=" ")
print()

num_of_queries = int(input())
queries = ["TAMBAH", "UBAH"]
for i in range(num_of_queries):
    query = random.choice(queries)
    print(query, end=" ")
    if query == "TAMBAH":
        print(random.randint(1, 1000_000_000))
    else:
        print(random.randint(1, num_of_saham), random.randint(1, 1000_000_000))
    