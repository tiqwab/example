//
// Created by nm on 5/4/19.
//

#include <stdbool.h>
#include <wait.h>

#ifndef DISPLAY_MANAGER_SAMPLE_PAM_H

bool login(const char *username, const char *password, pid_t *child_pid);

bool logout(void);

#define DISPLAY_MANAGER_SAMPLE_PAM_H

#endif //DISPLAY_MANAGER_SAMPLE_PAM_H
