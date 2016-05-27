#from captcha_solver import CaptchaSolver

#solver = CaptchaSolver('browser')
#with open('captcha.jpg', 'rb') as inp:
#    raw_data = inp.read()
#print(solver.solve_captcha(raw_data))

from PIL import Image

im = Image.open("captcha.jpg")
im = im.convert("P")

print im.histogram()