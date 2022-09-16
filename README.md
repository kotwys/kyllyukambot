# Kyllyukam bot

Udmurt dictionary bot for Telegram. It is a mirror of the
[Udmurt National Corpus](https://udmcorpus.udman.ru).

The bot requires Java Virtual Machine to be run.

To run it in webhooks mode, select `kyllyukambot.webhooks` as the main
classpath:

```bash
lein run -m kyllyukambot.webhooks

# Or, given the bot is already compiled to target/kyllyukambot.jar
java -cp target/kyllyukambot.jar clojure.main -m kyllyukambot.webhooks
```

Likewise, to run the bot in polling mode, select `kyllyukambot.polling` as the
main classpath.

## Configuration

The bot requires the following environment variables:

- `BOT_TOKEN`: Telegram bot token.
- (when in webhooks mode) `BOT_DOMAIN`: the domain Telegram will send requests
  to. The domain should have a valid certificate (wildcard certificates
  are also allowed).

## Development

*Project includes a [Nix package manager](https://nixos.org) [flake](flake.nix)
which would let you enter a version-locked development environment*.

To build the project, you need to have JDK and [Leiningen](https://leiningen.org)
installed on your system.

The bot can be built with all the dependencies included using the following comamnd:

```
lein uberjar
# kyllyukambot.jar should appear in the target/ directory
```