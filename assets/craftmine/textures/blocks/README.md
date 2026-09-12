# Blocos do atlas

O jogo não distribui texturas do Minecraft. Este diretório é o ponto de entrada para assets próprios.

A implementação atual cria uma textura RGBA procedural de 16×16 tiles (`TextureAtlas`) para o jogo funcionar sem arquivos externos. Um carregador pode procurar os nomes abaixo e substituir os slots correspondentes:

`stone`, `grass_top`, `grass_side`, `dirt`, `cobblestone`, `planks`, `log`, `leaves`, `sand`, `gravel`, `coal_ore`, `iron_ore`, `gold_ore`, `diamond_ore`, `glass`, `water`, `bedrock`, `crafting_table`, `furnace`, `torch`.

Crie PNGs próprios de 16×16 (ou altere `TextureAtlas`) e mantenha transparência somente nos blocos que realmente precisam dela. Nenhum arquivo oficial é necessário ou incluído.
