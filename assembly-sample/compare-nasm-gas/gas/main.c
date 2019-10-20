#include <stdio.h>

void hello();

void print_msg(char *msg) {
    printf("%s\n", msg);
}

int main(void) {
    hello();
    return 0;
}
