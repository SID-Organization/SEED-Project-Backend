var funcaoJs = function parseDelta(deltaString) {
    // Converter a string JSON para um objeto JavaScript
    var deltaJson = JSON.parse(deltaString);

    // Extrair o array JSON da chave "ops"
    var opsArray = deltaJson.ops;

    // Criar objeto JSON final
    var jsonObject = {ops: []};

    // Iterar sobre cada objeto no array "ops"
    for (var i = 0; i < opsArray.length; i++) {
        var op = opsArray[i];
        var insert = op.insert;
        var attributes = op.attributes;

        // Se o objeto contém a chave "attributes", adicionar as atribuições ao objeto JSON final
        if (attributes) {
            Object.keys(attributes).forEach(function (key) {
                var obj = {};
                obj[key] = attributes[key];
                jsonObject.ops.push({insert: "", attributes: obj});
            });
        }

        // Adicionar o objeto "insert" ao objeto JSON final
        if (insert) {
            jsonObject.ops.push({insert: insert});
        }
    }

    return deltaToHtml(jsonObject);
};


function deltaToHtml(delta) {
    var html = '';

    for (var i = 0; i < delta.ops.length; i++) {
        var op = delta.ops[i];
        if (op.insert) {
            if (typeof op.insert === 'string') {
                html += op.insert;
            } else {
                Object.keys(op.insert).forEach(function (key) {
                    switch (key) {
                        case 'header':
                            html += '<h' + op.insert[key] + '>';
                            break;
                        case 'blockquote':
                            html += '<blockquote>';
                            break;
                        case 'list':
                            if (op.insert.list === 'ordered') {
                                html += '<ol>';
                            } else {
                                html += '<ul>';
                            }
                            break;
                        case 'code-block':
                            html += '<pre><code>';
                            break;
                        case 'video':
                            html += '<video src="' + op.insert[key] + '"></video>';
                            break;
                        case 'image':
                            html += '<img src="' + op.insert[key] + '">';
                            break;
                        case 'formula':
                            html += '<span class="math-tex">' + op.insert[key] + '</span>';
                            break;
                    }
                });
            }
        } else if (op.attributes) {
            Object.keys(op.attributes).forEach(function (key) {
                switch (key) {
                    case 'bold':
                        if (op.attributes.bold) {
                            html += '<b>';
                        } else {
                            html += '</b>';
                        }
                        break;
                    case 'italic':
                        if (op.attributes.italic) {
                            html += '<i>';
                        } else {
                            html += '</i>';
                        }
                        break;
                    case 'underline':
                        if (op.attributes.underline) {
                            html += '<u>';
                        } else {
                            html += '</u>';
                        }
                        break;
                    case 'strike':
                        if (op.attributes.strike) {
                            html += '<strike>';
                        } else {
                            html += '</strike>';
                        }
                        break;
                    case 'link':
                        if (op.attributes.link) {
                            html += '<a href="' + op.attributes.link + '">';
                        } else {
                            html += '</a>';
                        }
                        break;
                    case 'code':
                        if (op.attributes.code) {
                            html += '<code>';
                        } else {
                            html += '</code>';
                        }
                        break;
                    case 'header':
                        if (op.attributes.header) {
                            html += '</h' + op.attributes.header + '>';
                        }
                        break;
                    case 'blockquote':
                        if (op.attributes.blockquote) {
                            html += '</blockquote>';
                        }
                        break;
                    case 'list':
                        if (op.attributes.list) {
                            if (op.attributes.list === 'ordered') {
                                html += '</ol>';
                            } else {
                                html += '</ul>';
                            }
                        }
                        break;
                    case 'code-block':
                        if (op.attributes['code-block']) {
                            html += '</code></pre>';
                        }
                        break;
                }
            });
        }
    }

    return html;
}
