export interface Owners {
  id: string;
  name: string;
  document: string;
  documentType: string;
  email: string;
}

export const OWNERS_OPTIONS: Owners[] = [
  {
    id: 'b12c9a11-3c44-4d91-9d2a-1f9a7c9f1001',
    name: 'Maria Oliveira',
    document: '111.444.777-35',
    documentType: 'CPF',
    email: 'maria.oliveira@example.com',
  },
  {
    id: 'c23d8b22-5e11-4a01-8a21-2a8b9c8d2002',
    name: 'João Silva',
    document: '22.333.444/0001-55',
    documentType: 'CNPJ',
    email: 'joao.silva@empresa.com',
  },
  {
    id: 'd34e7c33-6f22-4b12-9b32-3b9c0d9e3003',
    name: 'Ana Souza',
    document: '333.222.111-00',
    documentType: 'CPF',
    email: 'ana.souza@gmail.com',
  },
  {
    id: 'e45f6d44-7a33-4c23-8c43-4cad1eaf4004',
    name: 'Carlos Pereira',
    document: '44.555.666/0001-88',
    documentType: 'CNPJ',
    email: 'carlos@pereira.com.br',
  },
  {
    id: 'f56a7e55-8b44-4d34-9d54-5dbf2f0a5005',
    name: 'Fernanda Lima',
    document: '555.666.777-99',
    documentType: 'CPF',
    email: 'fernanda.lima@outlook.com',
  },
];
