# CHECKERS

**Autors:** Ernest Anguera Aixalà, Naïm Barba Morilla

## Contingut Destacat

1. **MiniMax i MiniMax-ID:**
   - Implementació d'un jugador automàtic utilitzant MiniMax per al joc de dames.
   - MiniMax-ID amb Iterative Deepening Search, Alpha-Beta Pruning i Zobrist Hashing per optimitzar la cerca.

2. **Heurística Implementada:**
   - Detalls sobre la heurística per avaluar l'estat del joc.
   - Consideracions per diversos factors com la quantitat de peces, presència de reines, proximitat als oponents, etc.
   - Ajustaments i bonificacions per millorar la presa de decisions.

3. **Comparació del Temps d'Execució:**
   - Anàlisi gràfica del temps d'execució entre MiniMax i MiniMax-ID en diferents nivells de profunditat.
    ![image](https://github.com/eur1p3des/Checkers/assets/72199195/53640904-b72d-43de-b87c-294f4e189e12)


4. **Nivells Baixats amb MiniMax-ID:**
   - Visualització de la relació entre temps disponible i nivells baixats amb MiniMax-ID.
     ![image](https://github.com/eur1p3des/Checkers/assets/72199195/28fabd82-3ec0-4dfe-881b-582efede0ff5)


5. **Estratègies d'Optimització:**
   - Ús de la poda Alpha-Beta per reduir el nombre de nodes explorats.
     ![image](https://github.com/eur1p3des/Checkers/assets/72199195/6ad75984-cf87-4589-958c-d9c584d34389)
   - Implementació del Zobrist Hashing per millorar l'eficiència de la poda i utilització d'un HashMap per emmagatzemar informació sobre estats del joc i millors moviments.
     ![image](https://github.com/eur1p3des/Checkers/assets/72199195/2f5ff97f-6243-4379-9e56-318e63dfa7cc)

6. **Repartiment de Tasques:**

   | Tasques          | Ernest | Naïm  |
   |------------------|--------|-------|
   | PlayerMiniMax    | 50%    | 50%   |
   | PlayerID         | 60%    | 40%   |
   | Heurística       | 40%    | 60%   |
   | Documentació     | 50%    | 50%   |


7. **Enllaç del Repositori GitHub:**
   - [Repositori GitHub](https://github.com/eur1p3des/Checkers.git)

## Resum General

La documentació detalla la implementació d'un jugador automàtic per al joc de dames utilitzant MiniMax i MiniMax-ID. S'inclou una heurística detallada per avaluar l'estat del joc, així com estratègies d'optimització com la poda Alpha-Beta i el Zobrist Hashing. L'anàlisi del temps d'execució i la comparació amb diferents nivells aporten una visió completa de l'eficàcia de l'algorisme. El repartiment de tasques i l'enllaç al repositori GitHub conclouen la documentació.
