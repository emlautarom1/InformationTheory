from PIL import Image
import urllib.request

print("Generating files...")

img = Image.new('RGB', (22000, 22000), (255, 0, 0))
img.save("red.png", "PNG")

with open("a.txt", "w+") as f:
    for i in range(1, 10000):
        f.write("A"*10000)
        f.write("\n")

urllib.request.urlretrieve("http://norvig.com/big.txt", "big.txt")

with open("hello.txt", "w+") as f:
    f.write("This is a sample file.\nHello world!")