{
  inputs.utils.url = "github:numtide/flake-utils";

  outputs = { self, nixpkgs, utils, ... }:
    utils.lib.eachDefaultSystem(system: 
      let pkgs = import nixpkgs { inherit system; };
      in {
        devShell = pkgs.mkShell {
          buildInputs = builtins.attrValues {
            inherit (pkgs) heroku;
            leiningen = pkgs.leiningen.override {
              jdk = pkgs.jdk8;
            };
          };
        };
      }
    );
}
