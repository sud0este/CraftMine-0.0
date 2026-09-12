# Arquitetura de runtime

## Ordem de um tick

1. `Minecraft` acumula tempo e chama exatamente até 20 ticks por segundo.
2. `World.tick` avança o relógio, atualiza entidades e executa o spawner.
3. `EntityPlayer` converte input em aceleração, aplica gravidade e chama a resolução AABB por eixo.
4. `World.setBlockState` notifica o bloco, recalcula a luz do chunk e incrementa a revisão do mesh.
5. O renderer interpola `prevPos*`/`pos*` somente no frame visual; gameplay nunca depende de OpenGL.

## Chunks

* Coordenadas globais usam `floorDiv/floorMod`, inclusive no lado negativo do mundo.
* Um chunk possui 16×256×16 posições, `short[]` para ids, nibble lógico para metadata e arrays separados para sky/block light.
* `ChunkProviderServer` gera sob demanda e indexa com uma chave `long` de `(chunkX, chunkZ)`.
* `MeshBuilder` visita cada bloco, elimina faces contra cubos opacos e escreve vertices intercalados diretamente em um VBO por chunk.

## Registro e dados

`BlockRegistry` e `ItemRegistry` são inicializados uma vez por `RegistryBootstrap`. As instâncias registradas são imutáveis; estado e quantidade ficam em `BlockState` e `ItemStack`. Isso evita objetos por voxel e mantém a atualização de bloco simples.

## Limites intencionais

O projeto usa placeholders procedurais e um renderer de cubos para entidades. Isso mantém o checkout livre de conteúdo proprietário e deixa os pontos de extensão explícitos: `TextureAtlas`, `StructureGenerator`, `PathNavigate`, `WorldSaveHandler` e a separação `World`/`WorldServer`. Não se apresenta como uma cópia binária nem como o código do Minecraft original.
