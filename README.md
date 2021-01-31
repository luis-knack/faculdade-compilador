# faculdade-compilador

Projeto Compilador UNICAP.

Sintaxe do Parser

<programa>        ::=   int main"("")" <bloco>
<bloco>           ::=   “{“ {<decl_var>}* {<comando>}* “}”
<comando>         ::=   <comando_básico> | <iteração> | if "("<expr_relacional>")" <comando> {else <comando>}?
<comando_básico>  ::=   <atribuição> | <bloco>
<iteração>        ::=   while "("<expr_relacional>")" <comando> | do <comando> while "("<expr_relacional>")"";"
<atribuição>      ::=   <id> "=" <expr_arit> ";"
<expr_relacional> ::=   <expr_arit> <op_relacional> <expr_arit>
<expr_arit>       ::=   <expr_arit> "+" <termo>   | <expr_arit> "-" <termo> | <termo>
<termo>           ::=   <termo> "*" <fator> | <termo> “/” <fator> | <fator>
<fator>           ::=   “(“ <expr_arit> “)” | <id> | <float> | <inteiro> | <char>
