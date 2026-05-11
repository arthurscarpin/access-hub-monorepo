# 🧠 Arquitetura ANPR (Automatic Number Plate Recognition)


## Fluxo de Processamento de Imagem de Veículos

---

# 1. Entrada no Spring Boot

### Endpoint

```
POST /access-events/image
```

### Responsabilidades:

* Receber imagem do veículo
* Salvar imagem (filesystem local ou S3/MinIO)
* Persistir metadados (MongoDB ou PostgreSQL)

### Exemplo de registro:

```json
{
  "imageId": "123",
  "path": "/images/123.jpg",
  "timestamp": "2026-05-03T10:00:00",
  "status": "PENDING_OCR"
}
```

---

# 2. Publicação de evento (Spring Boot → RabbitMQ)

### Fila: `ocr.queue`

### Payload enviado:

```json
{
  "imageId": "123",
  "path": "/images/123.jpg",
  "timestamp": "2026-05-03T10:00:00"
}
```

### Observação:

* NÃO enviar imagem no RabbitMQ
* Apenas referência ao arquivo

---

# 3. Serviço Python (ANPR Service)

### Responsabilidades:

* Consumir fila `ocr.queue`
* Buscar imagem no storage (filesystem ou S3)
* Executar YOLO (detecção de placa)
* Executar OCR (extração do texto)

### Resultado gerado:

```json
{
  "imageId": "123",
  "plate": "ABC1D23",
  "confidence": 0.98
}
```

---

# 4. Publicação do resultado (Python → RabbitMQ)

### Fila: `ocr.result.queue`

### Payload:

```json
{
  "imageId": "123",
  "plate": "ABC1D23",
  "confidence": 0.98
}
```

---

# 5. Consumo no Spring Boot (Finalização do fluxo)

### Responsabilidades:

* Consumir fila `ocr.result.queue`
* Criar AccessEvent
* Chamar `ValidateAccessUseCase`
* Persistir histórico de acesso

### Fluxo final:

* Validar veículo
* Gerar resultado de acesso (AUTHORIZED / DENIED)
* Salvar evento no sistema

---

# 🔁 Visão geral do fluxo

```
[Spring Boot API]
      ↓
Recebe imagem + salva metadata
      ↓
Publica evento (imageId)
      ↓
[RabbitMQ: ocr.queue]
      ↓

[Python ANPR Service]
      ↓
YOLO + OCR
      ↓
Publica resultado
      ↓
[RabbitMQ: ocr.result.queue]
      ↓

[Spring Boot Consumer]
      ↓
ValidateAccessUseCase
      ↓
Persistência de AccessEvent
```

---

# 🧩 Conceitos importantes

* Imagem nunca trafega via RabbitMQ
* Python é responsável apenas por visão computacional
* Spring Boot mantém regras de negócio
* Comunicação desacoplada via eventos

---

# 🚀 Resultado da arquitetura

* Escalável
* Resiliente a falhas
* Fácil de evoluir OCR separadamente
* Domínio limpo no Spring Boot
