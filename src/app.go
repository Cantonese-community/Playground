package main

import (
	"net/http"
	"html/template"
	"log"
)

func editor_page(w http.ResponseWriter, r *http.Request) {
	if r.Method == "GET" {
		t, _ := template.ParseFiles("../template/index.html")
		log.Println(t.Execute(w, nil))
	}
}

func main() {
	http.HandleFunc("/", editor_page)
	err := http.ListenAndServe(":9000", nil)
	if err != nil {
		log.Fatal("ListenAndServe: ", err)
	}
}