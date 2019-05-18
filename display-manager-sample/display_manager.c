#include <wait.h>
#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include "display_manager.h"

static pid_t x_server_pid;

void start_x_server(const char *display, const char *vt) {
    x_server_pid = fork();
    if (x_server_pid == 0) {
        char cmd[64];
        snprintf(cmd, sizeof(cmd), "/usr/bin/X %s %s -keeptty", display, vt);
        execl("/bin/bash", "/bin/bash", "-c", cmd, NULL);
        printf("Failed to start X server");
        exit(1);
    } else {
        sleep(1);
    }
}

void stop_x_server() {
    if (x_server_pid != 0) {
        kill(x_server_pid, SIGKILL);
    }
}

void sig_handler(int signo) {
    stop_x_server();
}
