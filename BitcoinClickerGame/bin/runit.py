import subprocess

for x in range(3):
    subprocess.Popen(["java", "GamePackage.Game", str(x), "&"])
