# Memofibo

# Submit via the interpreter a definition for a memofibo procedure that uses a
# chart. You are going to want the Nth value of the chart to hold the Nth
# fibonacci number if you've already computed it and to be empty otherwise.

chart = {1: 1, 2: 1}

def memofibo(n):
    v_n = chart.get(n)
    if v_n:
        return v_n

    v_n1 = memofibo(n-1)
    v_n2 = memofibo(n-2)

    v_n = v_n1 + v_n2
    chart[n] = v_n
    return v_n

print(memofibo(24))

