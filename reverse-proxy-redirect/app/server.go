package main

import (
	"fmt"
	"net/http"
)

func helloHandler(w http.ResponseWriter, r *http.Request) {
	fmt.Fprintf(w, "Hello, World\n")
}

func redirectRelativeHandler(w http.ResponseWriter, r *http.Request) {
	http.Redirect(w, r, "/", 302)
}

func redirectAbsoluteHandler(w http.ResponseWriter, r *http.Request) {
	http.Redirect(w, r, "http://app:18080/", 302)
}

func main() {
	http.HandleFunc("/", helloHandler)
	http.HandleFunc("/rd-rel", redirectRelativeHandler)
	http.HandleFunc("/rd-abs", redirectAbsoluteHandler)
	http.ListenAndServe(":18080", nil)
}
