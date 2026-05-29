export interface Owner {
  id: string;
  name: string;
  email: string;
  documentType: 'CPF' | 'RG';
  document: string;
}