export interface Vehicles {
    id: string;
    plate: string;
    model: string;
    status: string;
    email: string;
    ownerName: string;
}

export const VEHICLES_OPTIONS = [
  {
    id: '6fe86953-ce0c-4604-924e-ad0a2f1325bc',
    plate: 'POX4G22',
    model: 'Corolla',
    status: 'ACTIVE',
    ownerName: 'Maria Oliveira',
  },
  {
    id: 'a91c2b11-4f3a-4b91-9a12-8c1d2e3f4001',
    plate: 'BRA9K11',
    model: 'Civic',
    status: 'ACTIVE',
    ownerName: 'João Silva',
  },
  {
    id: 'b72d3c22-6a5b-4c22-9b33-1f2a3c4d5002',
    plate: 'XYZ1A99',
    model: 'HB20',
    status: 'INACTIVE',
    ownerName: 'Ana Souza',
  },
  {
    id: 'c83e4d33-7b6c-4d33-8c44-2f3b4c5d6003',
    plate: 'KLM7P88',
    model: 'Onix',
    status: 'ACTIVE',
    ownerName: 'Carlos Pereira',
  },
  {
    id: 'd94f5e44-8c7d-4e44-9d55-3g4h5i6j7004',
    plate: 'QWE3Z77',
    model: 'Gol',
    status: 'ACTIVE',
    ownerName: 'Fernanda Lima',
  },
];