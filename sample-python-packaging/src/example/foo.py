import requests

def greeting() -> str:
    ip = get_my_ip()
    return f"Hello World, your IP is {ip}"

def get_my_ip() -> str:
    res = requests.get('https://httpbin.org/ip')
    return res.json()['origin']
