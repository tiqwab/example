#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <netinet/udp.h>
#include <arpa/inet.h>

#define BUFSIZE 8192

int main(int argc, char *argv[]) {
    int s, len, ip_hlen;
    char buf[BUFSIZE];
    struct ip *ip;
    struct udphdr *udphdr;

    // protocol:
    // For PF_INET, standard well-defined IP protocols such as IPPROTO_UDP.
    // Defined in /usr/include/netinet/in.h
    // ip_p of struct ip
    if ((s = socket(PF_INET, SOCK_RAW, IPPROTO_UDP)) < 0) {
        perror("socket");
        exit(EXIT_FAILURE);
    }

    while (1) {
        // all of udp packet (including ip header) can be read
        if ((len = read(s, buf, BUFSIZE)) < 0) {
            perror("read");
            exit(EXIT_FAILURE);
        }

        ip = (struct ip *) buf;
        ip_hlen = ip->ip_hl << 2;
        udphdr = (struct udphdr *) ((char *) ip + ip_hlen);

        printf("src: %s:%d\n", inet_ntoa(ip->ip_src), ntohs(udphdr->uh_sport));
        printf("dst: %s:%d\n", inet_ntoa(ip->ip_dst), ntohs(udphdr->uh_dport));
    }
}
