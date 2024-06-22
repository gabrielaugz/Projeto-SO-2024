# SO-2024
 
<h2>1. Objetivo</h2> 
<br><ul>Implementar em Java um escalonador de processos que simule a execução virtuais de acordo com uma política de escalonamento que favoreça processos intensivos de processamento, mas que ao mesmo tempo em não cause estagnação em processos que façam muita entrada e saída.</ul></br>
<h2>2. Descrição</h2>
<ul>O simulador será composto por três threads de execução. O thread UserInterface implementa a
interface (gráfica) com o usuário do sistema. Através deste thread o usuário deverá prover os
comandos de controle de simulação. O thread LongTermScheduler representa o escalonador de longo
prazo do sistema. Este thread é responsável por receber as submissões de processos feitas ao
sistema e controlar a carga total no sistema. O thread ShortTermScheduler representa o escalonador
de curto prazo do sistema. Este thread, que deverá ser construído de acordo com a política de
escalonamento definida anteriormente e será responsável pela execução dos processos submetidos
ao simulador. Cada processo deverá ser descrito na linguagem de definição de programas
simulados (veja seção 4). A carga máxima de processos que o escalonador de curto prazo pode
suportar deve ser informada via linha de comando/parâmetro de execução quando da inicialização
do simulador. A interação entre os threads do sistema é realizada por meio de um conjunto de
interfaces. A Figura 1 ilustra a arquitetura do simulador de escalonamento.
O simulador deve ser inicializado com o valor em milissegundos de um quantum de tempo, e.g.,
200 ms equivalem a uma fatia de tempo. O escalonador de longo prazo deve manter uma fila dos
processos criados, mas ainda não admitidos no sistema. O escalonador de curto prazo deve manter
diferentes filas para armazenar os processos prontos, bloqueados (realizando entrada e saída) e
terminados.</ul>
