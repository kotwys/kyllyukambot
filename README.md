# Kyllyukam bot

Udmurt dictionary bot for Telegram. It's a mirror of
[Udmurt National Corpus](https://udmcorpus.udman.ru).

The bot is supposed to run on Heroku through webhooks. You can run it in polling
mode selecting `kyllyukambot.polled` as a main classpath.

```
lein run -m kyllyukambot.polled
```

## Development

Project includes a [Nix package manager](https://nixos.org) [flake](flake.nix)
which would let you enter a version-locked development environment.