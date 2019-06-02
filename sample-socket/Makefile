default: packet_dgram packet_raw inet_dgram inet_raw

packet_dgram: packet_dgram.c
	gcc -g -o $@ $<

packet_raw: packet_raw.c
	gcc -g -o $@ $<

inet_dgram: inet_dgram.c
	gcc -g -o $@ $<

inet_raw: inet_raw.c
	gcc -g -o $@ $<

clean:
	rm -f packet_dgram packet_raw inet_dgram inet_raw
