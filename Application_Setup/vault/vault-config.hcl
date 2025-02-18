storage "postgresql" {
  connection_url = "jdbc:postgresql://owner_chimera_application:owner_chimera_application@localhost:5432/chimera_db"
  table = "vault_kv_store"
}

listener "tcp" {
  address     = "0.0.0.0:8200"
  tls_disable = true
  # tls_cert_file = "/etc/vault.d/vault.crt"
  # tls_key_file  = "/etc/vault.d/vault.key"
}

# Use a file-based audit log
audit "file" {
  path = "/var/log/vault_audit.log"
}

# service_registration "istio" {
#   address = "http://istio-pilot.istio-system:8500"
# }

ui = true
default_lease_ttl = "168h"
max_lease_ttl = "720h"

