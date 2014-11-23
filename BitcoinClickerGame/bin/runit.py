import subprocess

for x in range(2):
    subprocess.Popen(["java", "GamePackage.Game", str(x), "&"])
