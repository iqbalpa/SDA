jml_query = int(input("jumlah perintah: "))
ans1 = []
ans2 = []

for i in range(jml_query):
    answer = int(input())
    ans1.append(answer)
for i in range(jml_query):
    answer = int(input())
    ans2.append(answer)

matches = 0
for i in range(jml_query):
    print(f"{ans1[i]:<3}||{ans2[i]:>3}     ===     {ans1[i] == ans2[i]}")
    if ans1[i] == ans2[i]:
        matches += 1

print()
print(f"matches: {matches}/{jml_query} ({matches/jml_query*100}%)")
