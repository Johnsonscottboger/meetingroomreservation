var util = {
    _styleStartRegex : /<style.*>/gmi,
    _styleRegex: /<style.*>[ \s\S]*?(?=<\/style>)/gmi,

    _styleAttrStartRegex : /<style/gmi,
    _styleAttrRegex: /<style .+(?=>)/gmi,

    _htmlStartRegex : /<template.*>/gmi,
    _htmlRegex: /<template.*>[\s\S]*(?=<\/template>)/gmi,

    _scriptStartRegex : /<script.*>/gmi,
    _scriptRegex: /<script.*>[ \s\S]*?(?=<\/script>)/gmi,

    /**
     * 解析组件
     */
    parseComponent: function (content, options) {
        if (!content) throw Error("content is null.");
        if (!options) options = {};
        var target = this;
        var styles = content.match(this._styleRegex).map(function (value) {
            return value.replace(target._styleStartRegex, "");
        });
        var styleAttrs = content.match(this._styleAttrRegex).map(function (value) {
            return value.replace(target._styleAttrStartRegex, "");
        });
        var htmls = content.match(this._htmlRegex).map(function (value) {
            return value.replace(target._htmlStartRegex, "");
        });
        var scripts = content.match(this._scriptRegex).map(function (value) {
            return value.replace(target._scriptStartRegex, "");
        });

        var style = "";
        if (styles != null && styles.length > 0) {
            style = "<component scoped :is=\"'style'\"";
            if (styleAttrs != null && styleAttrs.length > 0) {
                style += styleAttrs.join("");
            }
            style += ">";
            style += styles.join("");
            style += "</component>";
        }

        if (htmls == null || htmls.length <= 0) {
            throw Error("not found '<template>' tag.");
        }
        var html = htmls.join("");
        if (style) {
            var index = html.lastIndexOf("</");
            if (index !== -1) {
                html = html.substr(0, index) + style + html.substr(index);
            }
        }
        options.template = html;

        var script = scripts.join("");
        script = "(" + /{[\s\S]*}/gmi.exec(script) + ")";
        var obj = eval(script);
        for (var prop in obj) {
            options[prop] = obj[prop];
        }
        return options;
    },

    /**
     * 加载单文件组件
     */
    loadComponent: function (url, options) {
        var target = this;
        return function (resolve, reject) {
            axios.get(url)
                .then(function (response) {
                    var options = target.parseComponent(response.data, options);
                    resolve(options);
                });
            // $.get(url, function (response) {
            //     var options = target.parseComponent(response, options);
            //     resolve(options);
            // })
        }
    }
};