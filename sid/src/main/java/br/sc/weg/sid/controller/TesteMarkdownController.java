package br.sc.weg.sid.controller;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@CrossOrigin
@RequestMapping("/sid/api/testemarkdown")
public class TesteMarkdownController {

    @PostMapping
    public ResponseEntity<Object> deltaToMarkdown(@RequestBody String delta) {

        delta = "{\"ops\":[{\"insert\":\"Teste de markdown\"},{\"attributes\":{\"header\":1},\"insert\":\"\"";
        Parser parser = Parser.builder().build();
        Node document = parser.parse(delta);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String markdown = renderer.render(document);

        return ResponseEntity.ok(markdown);
    }

}
