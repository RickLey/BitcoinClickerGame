import subprocess

for x in range(4):
    subprocess.Popen(["java", "GamePackage.Game", str(x), "&"])
