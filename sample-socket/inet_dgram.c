#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>

#define BUFSIZE 8192
#define PORT 5320

int main(int argc, char *argv[]) {
    int s, len, ip_hlen;
    char buf[BUFSIZE];
    struct sockaddr_in server;

    // protocol:
    // For PF_INET, standard well-defined IP protocols such as IPPROTO_UDP.
    // Defined in /usr/include/netinet/in.h
    // ip_p of struct ip
    if ((s = socket(PF_INET, SOCK_DGRAM, 0)) < 0) {
        perror("socket");
        exit(EXIT_FAILURE);
    }

    memset(&server, 0, sizeof server);
    server.sin_family = PF_INET;
    server.sin_addr.s_addr = htons(INADDR_ANY); // htons(INADDR_LOOPBACK); // bind to lo
    server.sin_port = htons(PORT);
    if (bind(s, (struct sockaddr *) &server, sizeof server) < 0) {
        perror("bind");
        exit(EXIT_FAILURE);
    }

    while (1) {
        // only data is stored in buf (headers such as udphdr are excluded)
        if ((len = recvfrom(s, buf, BUFSIZE, 0, NULL, NULL)) < 0) {
            perror("read");
            exit(EXIT_FAILURE);
        }

        buf[len] = '\0';
        printf("received data: %s\n", buf);
    }
}
