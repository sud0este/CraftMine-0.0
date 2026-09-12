# CraftMine 1.8.9-style

CraftMine é uma implementação educacional e jogável de um sandbox voxel em **Java 8 + LWJGL 3**. A organização segue a mentalidade do cliente legado: registries estáticos, mundo/chunks carregados sob demanda, tick fixo de 20 TPS, servidor singleplayer no mesmo processo, meshing de faces visíveis e renderização imediata sem engine de jogo.

> **Escopo honesto:** este repositório não contém o código, assets ou texturas proprietárias do Minecraft. Reproduzir 100% do jogo original exigiria dezenas de milhares de classes e conteúdo licenciado. O projeto entrega um núcleo funcional e extensível: terreno procedural infinito, chunks 16×256×16, sky/block light, colisão, primeira pessoa, mineração/colocação, inventário, crafting 2×2 e mesa 3×3, hotbar, biomas, árvores, estruturas simples, ciclo dia/noite e mobs com IA básica (zumbi, esqueleto, creeper, aranha, vaca, porco, ovelha e galinha).

## Requisitos

* JDK 8 ou mais recente (JDK 17/21 também funciona).
* Maven 3.8+.
* OpenGL 3.3 e uma janela GLFW. O Maven baixa os bindings LWJGL; os natives são selecionados pelo profile do sistema operacional.

## Executar

```bash
mvn clean compile
mvn exec:java
```

Ou empacote e execute a classe `net.minecraft.client.Main` com o classpath gerado pelo Maven. A primeira inicialização cria apenas dados em memória; o mundo é deliberadamente novo a cada execução neste protótipo.

## Controles

* **W A S D**: mover; **Espaço**: pular; **Shift**: agachar; **Ctrl**: sprint.
* **Mouse**: olhar; botão esquerdo pressionado: minerar/atacar; botão direito: colocar o bloco selecionado.
* **1–9 / roda do mouse**: hotbar; **E**: inventário; **Esc**: liberar/capturar o mouse e fechar o inventário.
* O cursor começa capturado. Pressione `Esc` para liberá-lo; pressione `Esc` novamente para recapturar.

## Texturas e direitos autorais

Não há texturas oficiais neste projeto. `TextureAtlas` monta uma atlas procedural colorida para que o jogo rode imediatamente. Para substituir os placeholders, coloque PNGs próprios em:

```text
src/main/resources/assets/craftmine/textures/blocks/
```

O contrato de nomes e a reserva de slots estão documentados em `assets/craftmine/textures/blocks/README.md`. A camada `TextureAtlas` foi isolada justamente para que um carregador PNG possa ser conectado sem mudar blocos, chunks ou o renderer. Use somente arte produzido por você ou devidamente licenciado.

## Organização

```text
net.minecraft
├── block, item, crafting, registry       registries e gameplay de blocos/itens
├── entity                                 entidade, player, mobs e IA
├── world                                  World, Chunk, geração, biomas e luz
├── client                                loop, janela, input, GUI e renderização
└── util, util.math, util.tick              tipos pequenos sem dependências extras
```

A separação `World`/`ChunkProviderServer` permite evoluir o singleplayer para uma conexão cliente-servidor. A renderização usa VBO/VAO próprios, um mesh por chunk e culling de faces contra blocos opacos; não há LibGDX, Unity, jMonkey ou outra engine intermediária. O fluxo de tick e as invariantes de memória estão em [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md).
