import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IExecutionCode } from '../execution-code.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../execution-code.test-samples';

import { ExecutionCodeService } from './execution-code.service';

const requireRestSample: IExecutionCode = {
  ...sampleWithRequiredData,
};

describe('ExecutionCode Service', () => {
  let service: ExecutionCodeService;
  let httpMock: HttpTestingController;
  let expectedResult: IExecutionCode | IExecutionCode[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ExecutionCodeService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a ExecutionCode', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const executionCode = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(executionCode).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ExecutionCode', () => {
      const executionCode = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(executionCode).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ExecutionCode', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ExecutionCode', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ExecutionCode', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addExecutionCodeToCollectionIfMissing', () => {
      it('should add a ExecutionCode to an empty array', () => {
        const executionCode: IExecutionCode = sampleWithRequiredData;
        expectedResult = service.addExecutionCodeToCollectionIfMissing([], executionCode);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(executionCode);
      });

      it('should not add a ExecutionCode to an array that contains it', () => {
        const executionCode: IExecutionCode = sampleWithRequiredData;
        const executionCodeCollection: IExecutionCode[] = [
          {
            ...executionCode,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addExecutionCodeToCollectionIfMissing(executionCodeCollection, executionCode);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ExecutionCode to an array that doesn't contain it", () => {
        const executionCode: IExecutionCode = sampleWithRequiredData;
        const executionCodeCollection: IExecutionCode[] = [sampleWithPartialData];
        expectedResult = service.addExecutionCodeToCollectionIfMissing(executionCodeCollection, executionCode);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(executionCode);
      });

      it('should add only unique ExecutionCode to an array', () => {
        const executionCodeArray: IExecutionCode[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const executionCodeCollection: IExecutionCode[] = [sampleWithRequiredData];
        expectedResult = service.addExecutionCodeToCollectionIfMissing(executionCodeCollection, ...executionCodeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const executionCode: IExecutionCode = sampleWithRequiredData;
        const executionCode2: IExecutionCode = sampleWithPartialData;
        expectedResult = service.addExecutionCodeToCollectionIfMissing([], executionCode, executionCode2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(executionCode);
        expect(expectedResult).toContain(executionCode2);
      });

      it('should accept null and undefined values', () => {
        const executionCode: IExecutionCode = sampleWithRequiredData;
        expectedResult = service.addExecutionCodeToCollectionIfMissing([], null, executionCode, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(executionCode);
      });

      it('should return initial array if no ExecutionCode is added', () => {
        const executionCodeCollection: IExecutionCode[] = [sampleWithRequiredData];
        expectedResult = service.addExecutionCodeToCollectionIfMissing(executionCodeCollection, undefined, null);
        expect(expectedResult).toEqual(executionCodeCollection);
      });
    });

    describe('compareExecutionCode', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareExecutionCode(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareExecutionCode(entity1, entity2);
        const compareResult2 = service.compareExecutionCode(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareExecutionCode(entity1, entity2);
        const compareResult2 = service.compareExecutionCode(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareExecutionCode(entity1, entity2);
        const compareResult2 = service.compareExecutionCode(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
