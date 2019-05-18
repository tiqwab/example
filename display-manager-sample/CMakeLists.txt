cmake_minimum_required(VERSION 3.10.2)
project(display_manager_sample C)

set(CMAKE_C_STANDARD 99)

find_package(PkgConfig)
pkg_check_modules(GTK REQUIRED gtk+-3.0)

# for resources
set(UI_FILE "gui.ui")
configure_file(${UI_FILE} .)

# for PAM
find_path(PAM_INCLUDE_DIR NAME security/pam_appl.h PATH_SUFFIXES ../)
find_library(PAM_LIBRARY pam)

# set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -O0")
add_compile_options("-O0")

include_directories(
        ${GTK_INCLUDE_DIRS}
        ${PAM_INCLUDE_DIR}
)

add_executable(display_manager_sample main.c pam.c pam.h display_manager.c display_manager.h)

target_link_libraries(
        display_manager_sample
        ${GTK_LIBRARIES}
        "pthread"
        ${PAM_LIBRARY}
)
