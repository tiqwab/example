FROM ubuntu:18.04

RUN apt-get update

#
# Install sbt
# See https://www.scala-sbt.org/1.x/docs/Installing-sbt-on-Linux.html
#

# Install JDK
RUN apt-get install -y openjdk-8-jdk

# Install GnuPG and ca-certificates (for installing sbt)
RUN apt-get install -y gnupg ca-certificates

# Add repository for sbt
RUN echo "deb https://dl.bintray.com/sbt/debian /" | tee -a /etc/apt/sources.list.d/sbt.list

# Install public key from keyserver.ubuntu.com
RUN apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 \
                --recv 2EE0EA64E40A89B84B2DF73499E82A75642AC823

RUN apt-get update && apt-get install -y sbt

#
# Install necessary packages for scala-native
#

# libunwind is necessary for immix gc (maybe)
# libre2 is regular expression library and necessary for test
RUN apt-get install -y clang-7 libunwind-dev libre2-dev

# Additional native libraries which you need to compile and link
RUN apt-get install -y libyaml-dev

RUN ln -s /usr/bin/clang-7 /usr/bin/clang && \
    ln -s /usr/bin/clang++-7 /usr/bin/clang++
