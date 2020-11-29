import time

if __name__ == '__main__':
    with open('data/in.txt', 'r') as fr:
        with open('data/out.txt', 'w') as fw:
            for line in fr:
                print(line.strip())
                fw.write(line)
    time.sleep(10)
