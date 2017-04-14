import os
from PIL import Image


def resize(img, ratio):
    x, y = img.size
    nsize = x/ratio, y/ratio
    img.thumbnail(nsize, Image.ANTIALIAS)

if __name__ == "__main__":
    ratio = 4
    for f in os.listdir("."):
        if ".py" in f: continue
        print(f)
        new_f = "z" + f.replace(".png", "_resized.png")
        img = Image.open(f)
        resize(img, ratio)
        img.save(f)
