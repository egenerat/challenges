import unittest
from main import format_block, format_output

class TestCgxFormatter(unittest.TestCase):

    def test_format_block_empty(self):
        self.assertEqual(format_block('()', 0), [
            '(',
            ')'
            ])

    def test_format_block(self):
        self.assertEqual(format_block('(0)', 0), [
            '(',
            '    0',
            ')'
            ])

    def test_format_block_multiple_values(self):
        self.assertEqual(format_block('(0;1;2)', 0), [
            '(',
            '    0;',
            '    1;',
            '    2',
            ')'
            ])
    
    @unittest.skip("Code is not implemented yet")
    def test_format_block_containing_blocks(self):
        self.assertEqual(format_output("((true);(0))"), [
            '(',
            '   (',
            '       true',
            '   );',
            '   (',
            '       false',
            '   )'
        ])

    def test_format_output_block(self):
        self.assertEqual(format_output('(0)'), format_block('(0)', 0))


if __name__ == '__main__':
    unittest.main()
