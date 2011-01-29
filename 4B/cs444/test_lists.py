import os

p = [name.replace('pass_', '') for name in os.listdir('tests/pass')]
f = [name.replace('fail_', '') for name in os.listdir('tests/fail')]
a = [name.replace('all_', '').replace('.html.html', '.joos').replace('.html', '') for name in os.listdir('tests/all')]
print len(a) / 2

for t in a:
    if t not in p and t not in f:
        print 'Missing test: %s' % (t)
