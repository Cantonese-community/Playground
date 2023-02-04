package main

import (
	"net/http"
	"html/template"
	"log"
	"os/exec"
	"bytes"
)

func get_cantonese_version() string {
	cmd := exec.Command("cantonese", "-v")
	var stdout, stderr bytes.Buffer
	cmd.Stdout = &stdout
	cmd.Stderr = &stderr
	cmd.Run()
	outStr, errStr := string(stdout.Bytes()), string(stderr.Bytes())
	if len(errStr) != 0 {
		return errStr
	} else {
		return outStr
	}
}

func editor_page(w http.ResponseWriter, r *http.Request) {
	if r.Method == "GET" {
		version := get_cantonese_version()
		t, _ := template.ParseFiles("../template/index.html")
		log.Println(t.Execute(w, version))
	}
}

func main() {
	http.HandleFunc("/", editor_page)
	err := http.ListenAndServe(":9000", nil)
	if err != nil {
		log.Fatal("ListenAndServe: ", err)
	}
}