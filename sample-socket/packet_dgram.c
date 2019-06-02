#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netinet/ip.h>
#include <netinet/udp.h>
#include <arpa/inet.h>
#include <net/ethernet.h>

#define BUFSIZE 8192

int main(int argc, char *argv[]) {
    int s, len, ip_hlen;
    char buf[BUFSIZE];
    struct ip *ip;
    struct udphdr *udphdr;

    // protocol:
    // For PF_PACKET, Ethernet Protocol IDs such as ETH_P_IP and ETH_P_ARP
    // Defined in /usr/include/linux/if_either.h
    // ether_type of struct ether_header
    if ((s = socket(PF_PACKET, SOCK_DGRAM, htons(ETH_P_IP))) < 0) {
        perror("socket");
        exit(EXIT_FAILURE);
    }

    while (1) {
        // all of ip packet (excluding data link header) can be read
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
