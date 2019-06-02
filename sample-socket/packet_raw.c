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

static char *mac_addr(u_char *mac);

int main(int argc, char *argv[]) {
    int s, len, ip_hlen;
    char buf[BUFSIZE];
    struct ether_header * ethhdr;
    struct ip *ip;
    struct udphdr *udphdr;

    // protocol:
    // For PF_PACKET, Ethernet Protocol IDs such as ETH_P_IP and ETH_P_ARP
    // Defined in /usr/include/linux/if_either.h
    // ether_type of struct ether_header
    if ((s = socket(PF_PACKET, SOCK_RAW, htons(ETH_P_IP))) < 0) {
        perror("socket");
        exit(EXIT_FAILURE);
    }

    while (1) {
        // all of ip packet (including data link header) can be read
        if ((len = read(s, buf, BUFSIZE)) < 0) {
            perror("read");
            exit(EXIT_FAILURE);
        }

        ethhdr = (struct ether_header *) buf;
        ip = (struct ip *) ((char *) buf + sizeof(struct ether_header));
        ip_hlen = ip->ip_hl << 2;
        udphdr = (struct udphdr *) ((char *) ip + ip_hlen);

        printf("src: %s:%d (%s)\n", inet_ntoa(ip->ip_src), ntohs(udphdr->uh_sport),
                mac_addr(ethhdr->ether_shost));
        printf("dst: %s:%d (%s)\n", inet_ntoa(ip->ip_dst), ntohs(udphdr->uh_dport),
                mac_addr(ethhdr->ether_dhost));
    }
}

static char *mac_addr(u_char *mac) {
    static char str[18];

    snprintf(str, 18, "%02x:%02x:%02x:%02x:%02x:%02x",
            mac[0], mac[1], mac[2], mac[3], mac[4], mac[5]);

    return str;
}
