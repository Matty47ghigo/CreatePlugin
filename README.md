# CrateSystem Plugin

[![Spigot Version](https://img.shields.io/badge/Spigot-1.20+-brightgreen)](https://www.spigotmc.org/) [![License](https://img.shields.io/badge/license-MIT-blue)](LICENSE)

**CrateSystem** è un plugin per Minecraft Spigot che consente di creare e gestire crate (casse premio) con animazioni, hologrammi e premi personalizzati. Perfetto per server survival, minigame o economy!

## 🌟 Caratteristiche Principali

- **Creazione di Crate**: Crea crate personalizzate con nomi, premi e animazioni.
- **Premi Variabili**: Assegna premi casuali con probabilità configurabili.
- **Hologrammi**: Visualizza messaggi olografici sopra le crate.
- **Chiavi Virtuali e Fisiche**: Supporta chiavi virtuali salvate nel database e chiavi fisiche come oggetti nel gioco.
- **Database SQLite**: Salva i dati delle chiavi in un database SQLite per garantire persistenza.
- **Animazioni Particellari**: Aggiungi effetti particellari per rendere le crate più accattivanti.
- **Classifica delle Aperture**: Mostra una classifica dei giocatori che hanno aperto più crate.

---

## 📦 Installazione

### Requisiti
- Server Minecraft con **Spigot** o **Paper** (versione 1.20+).
- Java 17 o superiore.

### Passaggi
1. **Scarica il Plugin**:
   - Scarica l'ultima versione del plugin dalla sezione [Releases](https://github.com/Matty47ghigo/CreateSystem/releases).

2. **Aggiungi il Plugin al Server**:
   - Copia il file `.jar` nella cartella `plugins/` del tuo server.

3. **Avvia il Server**:
   - Avvia il server per generare automaticamente il file di configurazione (`config.yml`) e il database SQLite.

4. **Configura il Plugin**:
   - Modifica il file `config.yml` nella cartella `plugins/CrateSystem/` per personalizzare le crate, i premi e le impostazioni.

5. **Ricarica o Riavvia il Server**:
   - Usa il comando `/reload` o riavvia il server per applicare le modifiche.

---

## ⚙️ Configurazione

Il file `config.yml` contiene tutte le impostazioni del plugin. Ecco un esempio di configurazione:

```yaml
crates:
  legendary:
    name: '&6&lLEGENDARY CRATE'
    hologram:
      enabled: true
      text:
        - '&e&lLEGENDARY CRATE'
        - '&7Apri per ottenere premi fantastici!'
    rewards:
      - item: DIAMOND_BLOCK
        amount: 1
        chance: 0.1
      - item: GOLD_INGOT
        amount: 5
        chance: 0.5
    animations:
      type: PARTICLE_CIRCLE
      particle: REDSTONE
      color: FF0000
      size: 2.0
    guaranteed_rewards:
      - opens_required: 30
        reward:
          item: DIAMOND_BLOCK
          amount: 10
    location:
      world: world
      x: -69.0
      y: 70.0
      z: 14.0

keys:
  virtual:
    enabled: true
    database:
      type: SQLite
      path: database.db
  physical:
    item:
      material: TRIPWIRE_HOOK
      name: '&a&lCRATE KEY'
      lore:
        - '&7Usa questa chiave per aprire una crate.'
```

---

## 🎮 Comandi

| Comando           | Descrizione                                      | Permessi                  |
|--------------------|--------------------------------------------------|---------------------------|
| `/createcrate <nome>` | Crea una nuova crate.                          | `cratesystem.create`      |
| `/setcrate <nome>`    | Imposta la posizione di una crate.             | `cratesystem.setcrate`    |
| `/open <nome>`        | Apre una crate.                                | `cratesystem.open`        |
| `/givekey <giocatore> <nome>` | Dai una chiave a un giocatore.         | `cratesystem.givekey`     |
| `/leaderboard`        | Mostra la classifica delle aperture delle crate.| `cratesystem.leaderboard`|
| `/reloadcrates`       | Ricarica le crate dal file di configurazione.  | `cratesystem.reload`      |

---

## 📜 API

Se sei uno sviluppatore, puoi utilizzare l'API del plugin per integrare funzionalità aggiuntive. Ecco alcuni metodi principali:

- `plugin.getCrateManager().getCrate(String name)`: Ottieni una crate dal nome.
- `plugin.getCrateManager().openCrate(Player player, Crate crate)`: Apre una crate per un giocatore.
- `plugin.getSQLiteManager().addKey(UUID playerId, String crateName, int amount)`: Aggiunge chiavi virtuali a un giocatore.

Per ulteriori dettagli, consulta il codice sorgente.

---

## 🛠️ Contributi

I contributi sono benvenuti! Se vuoi migliorare il plugin o segnalare un bug:

1. Apri un **Issue** per segnalare problemi o suggerimenti.
2. Crea una **Pull Request** per proporre modifiche al codice.

---

## 📜 Licenza

Questo progetto è rilasciato sotto la licenza **MIT**. Vedi il file [LICENSE](LICENSE) per ulteriori dettagli.

---

## 🙏 Ringraziamenti

Grazie a:
- **DecentHolograms**: Per gli ologrammi.
- **SpigotMC Community**: Per il supporto e le risorse.

---

## 📞 Contatti

Se hai domande o vuoi collaborare, contattami su:
- Discord: `@matty47ghigo`
- Email: `matty47ghigo@gmail.com`
